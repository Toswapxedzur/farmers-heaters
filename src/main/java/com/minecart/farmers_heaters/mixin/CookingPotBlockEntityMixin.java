package com.minecart.farmers_heaters.mixin;

import com.minecart.farmers_heaters.AddonTags;
import com.minecart.farmers_heaters.mixin_interface.ICookingPotBlockEntity;
import com.minecart.farmers_heaters.mixin_interface.ISuperHeatable;
import com.minecart.farmers_heaters.mixin_interface.ISuperWithoutLevelHeatable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.Nameable;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;
import vectorwing.farmersdelight.common.block.entity.HeatableBlockEntity;
import vectorwing.farmersdelight.common.block.entity.SyncedBlockEntity;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;

import java.util.Map;
import java.util.Optional;

// Class-level remap = false: this mixin targets a Farmer's Delight class, and most of its
// shadowed members / injected methods are FD-specific (literal names in both dev and prod).
// IMPORTANT: any @Inject targeting a Minecraft-inherited method (e.g. load, saveAdditional)
// MUST set remap = true individually to override this class-level default — otherwise mixin
// looks for the literal MC name in production where it's been SRG-renamed (m_142466_, etc.).
@Debug(export = true)
@Mixin(value = CookingPotBlockEntity.class, remap = false)
public abstract class CookingPotBlockEntityMixin extends SyncedBlockEntity implements HeatableBlockEntity, Nameable, RecipeHolder, ICookingPotBlockEntity, ISuperWithoutLevelHeatable {

    @Shadow private int cookTime;
    @Shadow private int cookTimeTotal;
    @Shadow private ItemStack mealContainerStack;
    @Shadow @Final private ItemStackHandler inventory;
    @Shadow @Final public static Map<Item, Item> INGREDIENT_REMAINDER_OVERRIDES;

    @Shadow protected abstract void ejectIngredientRemainder(ItemStack remainderStack);
    @Shadow protected abstract boolean hasInput();
    @Shadow protected abstract boolean canCook(CookingPotRecipe recipe);

    // 1.20.1: Returns Optional<CookingPotRecipe> directly
    @Shadow protected abstract Optional<CookingPotRecipe> getMatchingRecipe(RecipeWrapper inventoryWrapper);

    @Shadow public abstract ItemStack getMeal();
    @Shadow protected abstract boolean doesMealHaveContainer(ItemStack meal);
    @Shadow protected abstract void moveMealToOutput();
    @Shadow protected abstract void useStoredContainersOnMeal();

    public CookingPotBlockEntityMixin(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
        super(tileEntityTypeIn, pos, state);
    }

    @Unique
    private int soulCookTime;

    // load() and saveAdditional() are inherited Minecraft methods. FD's override gets the
    // parent's SRG name (m_142466_ / m_183515_) in production, so remap = true is REQUIRED
    // to override the class-level remap = false above.
    @Inject(method = "load", at = @At(value = "TAIL"), remap = true)
    public void load(CompoundTag compound, CallbackInfo info) {
        this.soulCookTime = compound.getInt("SoulCookTime");
    }

    @Inject(method = "saveAdditional", at = @At(value = "TAIL"), remap = true)
    public void saveAdditional(CompoundTag compound, CallbackInfo info) {
        compound.putInt("SoulCookTime", this.soulCookTime);
    }

    @Inject(method = "cookingTick", at = @At("HEAD"), cancellable = true, remap = false)
    private static void farmers_heaters$cookingTick(Level level, BlockPos pos, BlockState state, CookingPotBlockEntity cookingPot, CallbackInfo ci) {
        ((ICookingPotBlockEntity) cookingPot).cook(level, pos, state);
        ci.cancel();
    }

    @Unique
    public void cook(Level level, BlockPos pos, BlockState state){
        boolean isHeated = isHeated(level, pos);
        boolean isSuperHeated = ((ISuperWithoutLevelHeatable)(Object)this).isSuperHeated();
        boolean cooked = false;
        boolean didInventoryChange = false;

        if (hasInput()) {
            Optional<CookingPotRecipe> recipeOptional = getMatchingRecipe(new RecipeWrapper(inventory));
            if (recipeOptional.isPresent()) {
                CookingPotRecipe recipe = recipeOptional.get();
                ItemStack result = recipe.getResultItem(getLevel().registryAccess());

                if(canCook(recipe)){
                    if(!result.is(AddonTags.Items.NETHER_FOOD) && isHeated){
                        ++cookTime;
                        soulCookTime = Mth.clamp(soulCookTime - 2, 0, cookTimeTotal);
                        didInventoryChange = processCooking(recipe, (CookingPotBlockEntity) (Object) this, false);
                        cooked = true;
                    } else if(result.is(AddonTags.Items.NETHER_FOOD) && isSuperHeated){
                        ++soulCookTime;
                        cookTime = Mth.clamp(cookTime - 2, 0, cookTimeTotal);
                        didInventoryChange = processCooking(recipe, (CookingPotBlockEntity) (Object) this, true);
                        cooked = true;
                    }
                }
            }
        }

        if (!cooked) {
            cookTime = Mth.clamp(cookTime - 2, 0, cookTimeTotal);
            soulCookTime = Mth.clamp(soulCookTime - 2, 0, cookTimeTotal);
        }

        ItemStack mealStack = getMeal();
        if (!mealStack.isEmpty()) {
            if (!doesMealHaveContainer(mealStack)) {
                moveMealToOutput();
                didInventoryChange = true;
            } else if (!inventory.getStackInSlot(7).isEmpty()) {
                useStoredContainersOnMeal();
                didInventoryChange = true;
            }
        }

        if (didInventoryChange) {
            inventoryChanged();
        }
    }

    @Unique
    public boolean processCooking(CookingPotRecipe recipe, CookingPotBlockEntity cookingPot, boolean isSoul) {
        if (this.level == null) {
            return false;
        } else {
            this.cookTimeTotal = recipe.getCookTime();
            if (!isSoul && this.cookTime < this.cookTimeTotal || isSoul && this.soulCookTime < this.cookTimeTotal) {
                return false;
            } else {
                if(isSoul)
                    this.soulCookTime = 0;
                else
                    this.cookTime = 0;

                this.mealContainerStack = recipe.getOutputContainer();

                // 1.20.1 assembly
                ItemStack resultStack = recipe.assemble(new RecipeWrapper(this.inventory), this.level.registryAccess());
                ItemStack storedMealStack = this.inventory.getStackInSlot(6);

                if (storedMealStack.isEmpty()) {
                    this.inventory.setStackInSlot(6, resultStack.copy());
                } else if (ItemStack.isSameItem(storedMealStack, resultStack)) {
                    storedMealStack.grow(resultStack.getCount());
                }

                // 1.20.1 Recipe set
                cookingPot.setRecipeUsed(recipe);

                for(int i = 0; i < 6; ++i) {
                    ItemStack slotStack = this.inventory.getStackInSlot(i);
                    // 1.20.1 uses hasCraftingRemainingItem
                    if (slotStack.hasCraftingRemainingItem()) {
                        this.ejectIngredientRemainder(slotStack.getCraftingRemainingItem());
                    } else if (INGREDIENT_REMAINDER_OVERRIDES.containsKey(slotStack.getItem())) {
                        this.ejectIngredientRemainder(INGREDIENT_REMAINDER_OVERRIDES.get(slotStack.getItem()).getDefaultInstance());
                    }

                    if (!slotStack.isEmpty()) {
                        slotStack.shrink(1);
                    }
                }

                return true;
            }
        }
    }

    @Overwrite
    public ContainerData createIntArray() {
        return new ContainerData() {
            public int get(int index) {
                int ret;
                switch (index) {
                    case 0 -> ret = cookTime;
                    case 1 -> ret = cookTimeTotal;
                    case 2 -> ret = soulCookTime;
                    default -> ret = 0;
                }
                return ret;
            }
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> cookTime = value;
                    case 1 -> cookTimeTotal = value;
                    case 2 -> soulCookTime = value;
                }
            }
            public int getCount() {
                return 3;
            }
        };
    }

    @Unique
    public boolean isSuperHeated() {
        return this.level == null ? false : ((ISuperHeatable) this).isSuperHeated(this.level, this.worldPosition);
    }
}

package com.meacks.table_game.assets.blocks;

import com.meacks.table_game.assets.blockEntities.MinoLargeTableBlockEntity;
import com.meacks.table_game.assets.handlers.ItemHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.meacks.table_game.assets.items.MinoHandCard.getBasicStack;

public class MinoLargeTable extends BaseEntityBlock {
    private static VoxelShape shape = Block.box(0, 14, 0, 16, 15, 16);

    public MinoLargeTable() {
        super(Properties.of().mapColor(MapColor.WOOD).instabreak().instrument(NoteBlockInstrument.BELL));
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
        return shape;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState blockState) {
        return new MinoLargeTableBlockEntity(pos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return super.getTicker(p_153212_, p_153213_, p_153214_);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> GameEventListener getListener(ServerLevel p_221121_, T p_221122_) {
        return super.getListener(p_221121_, p_221122_);
    }


    public static boolean changeColor(int clr, Level level, BlockPos blockPos){
        MinoLargeTableBlockEntity entity = (MinoLargeTableBlockEntity) level.getBlockEntity(blockPos);
        assert entity != null;
        return entity.changeColor(clr);
    }

    public static void getInitialCard(Player player, InteractionHand hand, Level level, BlockPos blockPos){
        NonNullList<ItemStack> itemStacks = player.getInventory().items;
        if(level.isClientSide())return;
        for (ItemStack itemStack : itemStacks) if (itemStack.is(ItemHandler.mino_hand_card.get())) return;
        MinoLargeTableBlockEntity entity = (MinoLargeTableBlockEntity) level.getBlockEntity(blockPos);
        assert entity != null;
        if(entity.shouldInitialGive()){
            ItemStack stack = entity.dealPlayerCards(7,getBasicStack());
            stack = entity.signature(stack);
            player.setItemInHand(hand,stack);
        }
    }
    @SuppressWarnings("deprecation")
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack stack=player.getItemInHand(hand);
        if(stack.is(Items.CREEPER_HEAD)){//0
            if(changeColor(0,level,pos)) player.setItemInHand(hand,Items.AIR.getDefaultInstance());
        } else if (stack.is(Items.ORANGE_DYE)) {//2
            if(changeColor(2,level,pos)) player.setItemInHand(hand,Items.AIR.getDefaultInstance());
        } else if (stack.is(Items.REDSTONE)) {//3
            if(changeColor(3,level,pos)) player.setItemInHand(hand,Items.AIR.getDefaultInstance());
        } else if (stack.is(Items.DIAMOND)) {//1
            if(changeColor(1,level,pos)) player.setItemInHand(hand,Items.AIR.getDefaultInstance());
        } else if (stack.is(Items.AIR)) {
            getInitialCard(player,hand,level,pos);
        }
        return InteractionResult.SUCCESS;
    }

}

plugins {
    id("fuzs.multiloader.multiloader-convention-plugins-common")
}

dependencies {
    modCompileOnlyApi(sharedLibs.puzzleslib.common)
}

multiloader {
    mixins {
        mixin("LivingEntityMixin", "PlayerMixin", "ServerGamePacketListenerImplMixin", "ServerPlayerMixin")
        clientMixin("EntityMixin", "PlayerModelMixin", "PlayerRendererMixin")
    }
}

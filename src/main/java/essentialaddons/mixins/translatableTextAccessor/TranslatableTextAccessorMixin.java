package essentialaddons.mixins.translatableTextAccessor;

import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(TranslatableText.class)
public interface TranslatableTextAccessorMixin {
    @Accessor("translations")
    List<Text> getTextTranslations();

    @Invoker("setTranslation")
    void setTextTranslation(String translation);
}

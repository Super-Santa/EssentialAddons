package essentialaddons.mixins.translatableTextAccessor;

import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;
import java.util.function.Consumer;

@Mixin(value = TranslatableText.class)
public interface TranslatableTextAccessorMixin {
    @Accessor("translations")
    void setTextTranslations(List<StringVisitable> translations);

    @Accessor("translations")
    List<StringVisitable> getTextTranslations();

    @Invoker()
    void setForEachPart(String translation, Consumer<StringVisitable> partsConsumer);

}
package bot.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) // A anotação estará disponível em tempo de execução
@Target(ElementType.TYPE) // A anotação pode ser usada apenas em classes
public @interface RegisterCommand {
}
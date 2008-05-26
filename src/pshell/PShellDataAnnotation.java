package pshell;

import java.lang.annotation.*;


/**
 * Annotazione per i metodi di Shell.
 * 
 * @author Parcman Tm
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PShellDataAnnotation
{
    String name();
    String info() default "[unassigned]";
    String help() default "[unassigned]";
}


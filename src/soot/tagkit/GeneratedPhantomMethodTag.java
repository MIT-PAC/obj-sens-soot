package soot.tagkit;

/**
 * Tag for methods that were created by soot when a method body was required, 
 * but the method definition could not be found.
 * 
 * @author mgordon
 *
 */
public class GeneratedPhantomMethodTag extends StringTag {
    public static final String name = "GENERATED_PHANTOM_METHOD";
  
    
    public GeneratedPhantomMethodTag(){
        super(name);
    }
}

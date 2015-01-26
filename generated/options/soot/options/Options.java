
/* Soot - a J*va Optimization Framework
 * Copyright (C) 2003 Ondrej Lhotak
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 */

/* THIS FILE IS AUTO-GENERATED FROM soot_options.xml. DO NOT MODIFY. */

package soot.options;
import soot.*;
import java.util.*;
import soot.PackManager;

/** Soot command-line options parser.
 * @author Ondrej Lhotak
 */

public class Options extends OptionsBase {
    public Options(Singletons.Global g) { }
    public static Options v() { return G.v().soot_options_Options(); }


    public static final int src_prec_c = 1;
    public static final int src_prec_class = 1;
    public static final int src_prec_only_class = 2;
    public static final int src_prec_J = 3;
    public static final int src_prec_jimple = 3;
    public static final int src_prec_java = 4;
    public static final int src_prec_apk = 5;
    public static final int output_format_J = 1;
    public static final int output_format_jimple = 1;
    public static final int output_format_j = 2;
    public static final int output_format_jimp = 2;
    public static final int output_format_S = 3;
    public static final int output_format_shimple = 3;
    public static final int output_format_s = 4;
    public static final int output_format_shimp = 4;
    public static final int output_format_B = 5;
    public static final int output_format_baf = 5;
    public static final int output_format_b = 6;
    public static final int output_format_G = 7;
    public static final int output_format_grimple = 7;
    public static final int output_format_g = 8;
    public static final int output_format_grimp = 8;
    public static final int output_format_X = 9;
    public static final int output_format_xml = 9;
    public static final int output_format_dex = 10;
    public static final int output_format_n = 11;
    public static final int output_format_none = 11;
    public static final int output_format_jasmin = 12;
    public static final int output_format_c = 13;
    public static final int output_format_class = 13;
    public static final int output_format_d = 14;
    public static final int output_format_dava = 14;
    public static final int output_format_t = 15;
    public static final int output_format_template = 15;
    public static final int throw_analysis_pedantic = 1;
    public static final int throw_analysis_unit = 2;
    public static final int check_init_throw_analysis_auto = 1;
    public static final int check_init_throw_analysis_pedantic = 2;
    public static final int check_init_throw_analysis_unit = 3;
    public static final int check_init_throw_analysis_dalvik = 4;

    @SuppressWarnings("unused")
    public boolean parse( String[] argv ) {
        LinkedList<String> phaseOptions = new LinkedList<String>();

        for( int i = argv.length; i > 0; i-- ) {
            pushOptions( argv[i-1] );
        }
        while( hasMoreOptions() ) {
            String option = nextOption();
            if( option.charAt(0) != '-' ) {
                classes.add( option );
                continue;
            }
            while( option.charAt(0) == '-' ) {
                option = option.substring(1);
            }
            if( false );

            else if( false 
            || option.equals( "coffi" )
            )
                coffi = true;
  
            else if( false 
            || option.equals( "h" )
            || option.equals( "help" )
            )
                help = true;
  
            else if( false 
            || option.equals( "pl" )
            || option.equals( "phase-list" )
            )
                phase_list = true;
  
            else if( false
            || option.equals( "ph" )
            || option.equals( "phase-help" )
            ) {
                if( !hasMoreOptions() ) {
                    G.v().out.println( "No value given for option -"+option );
                    return false;
                }
                String value = nextOption();
    
                if( phase_help == null )
                    phase_help = new LinkedList<String>();

                phase_help.add( value );
                
            }
  
            else if( false 
            || option.equals( "version" )
            )
                version = true;
  
            else if( false 
            || option.equals( "v" )
            || option.equals( "verbose" )
            )
                verbose = true;
  
            else if( false 
            || option.equals( "interactive-mode" )
            )
                interactive_mode = true;
  
            else if( false 
            || option.equals( "unfriendly-mode" )
            )
                unfriendly_mode = true;
  
            else if( false 
            || option.equals( "app" )
            )
                app = true;
  
            else if( false 
            || option.equals( "w" )
            || option.equals( "whole-program" )
            )
                whole_program = true;
  
            else if( false 
            || option.equals( "ws" )
            || option.equals( "whole-shimple" )
            )
                whole_shimple = true;
  
            else if( false 
            || option.equals( "fly" )
            || option.equals( "on-the-fly" )
            )
                on_the_fly = true;
  
            else if( false 
            || option.equals( "validate" )
            )
                validate = true;
  
            else if( false 
            || option.equals( "debug" )
            )
                debug = true;
  
            else if( false 
            || option.equals( "debug-resolver" )
            )
                debug_resolver = true;
  
            else if( false
            || option.equals( "cp" )
            || option.equals( "soot-class-path" )
            || option.equals( "soot-classpath" )
            ) {
                if( !hasMoreOptions() ) {
                    G.v().out.println( "No value given for option -"+option );
                    return false;
                }
                String value = nextOption();
    
                if( soot_classpath.length() == 0 )
                    soot_classpath = value;
                else {
                    G.v().out.println( "Duplicate values "+soot_classpath+" and "+value+" for option -"+option );
                    return false;
                }
            }
  
            else if( false 
            || option.equals( "pp" )
            || option.equals( "prepend-classpath" )
            )
                prepend_classpath = true;
  
            else if( false
            || option.equals( "process-path" )
            || option.equals( "process-dir" )
            ) {
                if( !hasMoreOptions() ) {
                    G.v().out.println( "No value given for option -"+option );
                    return false;
                }
                String value = nextOption();
    
                if( process_dir == null )
                    process_dir = new LinkedList<String>();

                process_dir.add( value );
                
            }
  
            else if( false 
            || option.equals( "oaat" )
            )
                oaat = true;
  
            else if( false
            || option.equals( "android-jars" )
            ) {
                if( !hasMoreOptions() ) {
                    G.v().out.println( "No value given for option -"+option );
                    return false;
                }
                String value = nextOption();
    
                if( android_jars.length() == 0 )
                    android_jars = value;
                else {
                    G.v().out.println( "Duplicate values "+android_jars+" and "+value+" for option -"+option );
                    return false;
                }
            }
  
            else if( false
            || option.equals( "force-android-jar" )
            ) {
                if( !hasMoreOptions() ) {
                    G.v().out.println( "No value given for option -"+option );
                    return false;
                }
                String value = nextOption();
    
                if( force_android_jar.length() == 0 )
                    force_android_jar = value;
                else {
                    G.v().out.println( "Duplicate values "+force_android_jar+" and "+value+" for option -"+option );
                    return false;
                }
            }
  
            else if( false 
            || option.equals( "ast-metrics" )
            )
                ast_metrics = true;
  
            else if( false
            || option.equals( "src-prec" )
            ) {
                if( !hasMoreOptions() ) {
                    G.v().out.println( "No value given for option -"+option );
                    return false;
                }
                String value = nextOption();
    
                if( false );
    
                else if( false
                || value.equals( "c" )
                || value.equals( "class" )
                ) {
                    if( src_prec != 0
                    && src_prec != src_prec_class ) {
                        G.v().out.println( "Multiple values given for option "+option );
                        return false;
                    }
                    src_prec = src_prec_class;
                }
    
                else if( false
                || value.equals( "only-class" )
                ) {
                    if( src_prec != 0
                    && src_prec != src_prec_only_class ) {
                        G.v().out.println( "Multiple values given for option "+option );
                        return false;
                    }
                    src_prec = src_prec_only_class;
                }
    
                else if( false
                || value.equals( "J" )
                || value.equals( "jimple" )
                ) {
                    if( src_prec != 0
                    && src_prec != src_prec_jimple ) {
                        G.v().out.println( "Multiple values given for option "+option );
                        return false;
                    }
                    src_prec = src_prec_jimple;
                }
    
                else if( false
                || value.equals( "java" )
                ) {
                    if( src_prec != 0
                    && src_prec != src_prec_java ) {
                        G.v().out.println( "Multiple values given for option "+option );
                        return false;
                    }
                    src_prec = src_prec_java;
                }
    
                else if( false
                || value.equals( "apk" )
                ) {
                    if( src_prec != 0
                    && src_prec != src_prec_apk ) {
                        G.v().out.println( "Multiple values given for option "+option );
                        return false;
                    }
                    src_prec = src_prec_apk;
                }
    
                else {
                    G.v().out.println( "Invalid value "+value+" given for option -"+option );
                    return false;
                }
           }
  
            else if( false 
            || option.equals( "full-resolver" )
            )
                full_resolver = true;
  
            else if( false 
            || option.equals( "allow-phantom-refs" )
            )
                allow_phantom_refs = true;
  
            else if( false 
            || option.equals( "no-bodies-for-excluded" )
            )
                no_bodies_for_excluded = true;
  
            else if( false 
            || option.equals( "j2me" )
            )
                j2me = true;
  
            else if( false
            || option.equals( "main-class" )
            ) {
                if( !hasMoreOptions() ) {
                    G.v().out.println( "No value given for option -"+option );
                    return false;
                }
                String value = nextOption();
    
                if( main_class.length() == 0 )
                    main_class = value;
                else {
                    G.v().out.println( "Duplicate values "+main_class+" and "+value+" for option -"+option );
                    return false;
                }
            }
  
            else if( false 
            || option.equals( "polyglot" )
            )
                polyglot = true;
  
            else if( false
            || option.equals( "d" )
            || option.equals( "output-dir" )
            ) {
                if( !hasMoreOptions() ) {
                    G.v().out.println( "No value given for option -"+option );
                    return false;
                }
                String value = nextOption();
    
                if( output_dir.length() == 0 )
                    output_dir = value;
                else {
                    G.v().out.println( "Duplicate values "+output_dir+" and "+value+" for option -"+option );
                    return false;
                }
            }
  
            else if( false
            || option.equals( "f" )
            || option.equals( "output-format" )
            ) {
                if( !hasMoreOptions() ) {
                    G.v().out.println( "No value given for option -"+option );
                    return false;
                }
                String value = nextOption();
    
                if( false );
    
                else if( false
                || value.equals( "J" )
                || value.equals( "jimple" )
                ) {
                    if( output_format != 0
                    && output_format != output_format_jimple ) {
                        G.v().out.println( "Multiple values given for option "+option );
                        return false;
                    }
                    output_format = output_format_jimple;
                }
    
                else if( false
                || value.equals( "j" )
                || value.equals( "jimp" )
                ) {
                    if( output_format != 0
                    && output_format != output_format_jimp ) {
                        G.v().out.println( "Multiple values given for option "+option );
                        return false;
                    }
                    output_format = output_format_jimp;
                }
    
                else if( false
                || value.equals( "S" )
                || value.equals( "shimple" )
                ) {
                    if( output_format != 0
                    && output_format != output_format_shimple ) {
                        G.v().out.println( "Multiple values given for option "+option );
                        return false;
                    }
                    output_format = output_format_shimple;
                }
    
                else if( false
                || value.equals( "s" )
                || value.equals( "shimp" )
                ) {
                    if( output_format != 0
                    && output_format != output_format_shimp ) {
                        G.v().out.println( "Multiple values given for option "+option );
                        return false;
                    }
                    output_format = output_format_shimp;
                }
    
                else if( false
                || value.equals( "B" )
                || value.equals( "baf" )
                ) {
                    if( output_format != 0
                    && output_format != output_format_baf ) {
                        G.v().out.println( "Multiple values given for option "+option );
                        return false;
                    }
                    output_format = output_format_baf;
                }
    
                else if( false
                || value.equals( "b" )
                ) {
                    if( output_format != 0
                    && output_format != output_format_b ) {
                        G.v().out.println( "Multiple values given for option "+option );
                        return false;
                    }
                    output_format = output_format_b;
                }
    
                else if( false
                || value.equals( "G" )
                || value.equals( "grimple" )
                ) {
                    if( output_format != 0
                    && output_format != output_format_grimple ) {
                        G.v().out.println( "Multiple values given for option "+option );
                        return false;
                    }
                    output_format = output_format_grimple;
                }
    
                else if( false
                || value.equals( "g" )
                || value.equals( "grimp" )
                ) {
                    if( output_format != 0
                    && output_format != output_format_grimp ) {
                        G.v().out.println( "Multiple values given for option "+option );
                        return false;
                    }
                    output_format = output_format_grimp;
                }
    
                else if( false
                || value.equals( "X" )
                || value.equals( "xml" )
                ) {
                    if( output_format != 0
                    && output_format != output_format_xml ) {
                        G.v().out.println( "Multiple values given for option "+option );
                        return false;
                    }
                    output_format = output_format_xml;
                }
    
                else if( false
                || value.equals( "dex" )
                ) {
                    if( output_format != 0
                    && output_format != output_format_dex ) {
                        G.v().out.println( "Multiple values given for option "+option );
                        return false;
                    }
                    output_format = output_format_dex;
                }
    
                else if( false
                || value.equals( "n" )
                || value.equals( "none" )
                ) {
                    if( output_format != 0
                    && output_format != output_format_none ) {
                        G.v().out.println( "Multiple values given for option "+option );
                        return false;
                    }
                    output_format = output_format_none;
                }
    
                else if( false
                || value.equals( "jasmin" )
                ) {
                    if( output_format != 0
                    && output_format != output_format_jasmin ) {
                        G.v().out.println( "Multiple values given for option "+option );
                        return false;
                    }
                    output_format = output_format_jasmin;
                }
    
                else if( false
                || value.equals( "c" )
                || value.equals( "class" )
                ) {
                    if( output_format != 0
                    && output_format != output_format_class ) {
                        G.v().out.println( "Multiple values given for option "+option );
                        return false;
                    }
                    output_format = output_format_class;
                }
    
                else if( false
                || value.equals( "d" )
                || value.equals( "dava" )
                ) {
                    if( output_format != 0
                    && output_format != output_format_dava ) {
                        G.v().out.println( "Multiple values given for option "+option );
                        return false;
                    }
                    output_format = output_format_dava;
                }
    
                else if( false
                || value.equals( "t" )
                || value.equals( "template" )
                ) {
                    if( output_format != 0
                    && output_format != output_format_template ) {
                        G.v().out.println( "Multiple values given for option "+option );
                        return false;
                    }
                    output_format = output_format_template;
                }
    
                else {
                    G.v().out.println( "Invalid value "+value+" given for option -"+option );
                    return false;
                }
           }
  
            else if( false 
            || option.equals( "outjar" )
            || option.equals( "output-jar" )
            )
                output_jar = true;
  
            else if( false 
            || option.equals( "xml-attributes" )
            )
                xml_attributes = true;
  
            else if( false 
            || option.equals( "print-tags" )
            || option.equals( "print-tags-in-output" )
            )
                print_tags_in_output = true;
  
            else if( false 
            || option.equals( "no-output-source-file-attribute" )
            )
                no_output_source_file_attribute = true;
  
            else if( false 
            || option.equals( "no-output-inner-classes-attribute" )
            )
                no_output_inner_classes_attribute = true;
  
            else if( false
            || option.equals( "dump-body" )
            ) {
                if( !hasMoreOptions() ) {
                    G.v().out.println( "No value given for option -"+option );
                    return false;
                }
                String value = nextOption();
    
                if( dump_body == null )
                    dump_body = new LinkedList<String>();

                dump_body.add( value );
                
            }
  
            else if( false
            || option.equals( "dump-cfg" )
            ) {
                if( !hasMoreOptions() ) {
                    G.v().out.println( "No value given for option -"+option );
                    return false;
                }
                String value = nextOption();
    
                if( dump_cfg == null )
                    dump_cfg = new LinkedList<String>();

                dump_cfg.add( value );
                
            }
  
            else if( false 
            || option.equals( "show-exception-dests" )
            )
                show_exception_dests = true;
  
            else if( false 
            || option.equals( "gzip" )
            )
                gzip = true;
  
            else if( false 
            || option.equals( "force-overwrite" )
            )
                force_overwrite = true;
  
            else if( false
            || option.equals( "plugin" )
            ) {
                if( !hasMoreOptions() ) {
                    G.v().out.println( "No value given for option -"+option );
                    return false;
                }
                String value = nextOption();
    
                if( plugin == null )
                    plugin = new LinkedList<String>();

                plugin.add( value );
                
                if(!loadPluginConfiguration(value)) {
                    G.v().out.println( "Failed to load plugin" +value );
                    return false;
                }
                
            }
  
            else if( false
            || option.equals( "p" )
            || option.equals( "phase-option" )
            ) {
                if( !hasMoreOptions() ) {
                    G.v().out.println( "No phase name given for option -"+option );
                    return false;
                }
                String phaseName = nextOption();
                if( !hasMoreOptions() ) {
                    G.v().out.println( "No phase option given for option -"+option+" "+phaseName );
                    return false;
                }
                String phaseOption = nextOption();
    
                phaseOptions.add( phaseName );
                phaseOptions.add( phaseOption );
            }
  
            else if( false
            || option.equals( "O" )
            || option.equals( "optimize" )
            ) {
                
                pushOptions( "enabled:true" );
                pushOptions( "sop" );
                pushOptions( "-p" );
                pushOptions( "enabled:true" );
                pushOptions( "jop" );
                pushOptions( "-p" );
                pushOptions( "enabled:true" );
                pushOptions( "gop" );
                pushOptions( "-p" );
                pushOptions( "enabled:true" );
                pushOptions( "bop" );
                pushOptions( "-p" );
                pushOptions( "only-stack-locals:false" );
                pushOptions( "gb.a2" );
                pushOptions( "-p" );
                pushOptions( "only-stack-locals:false" );
                pushOptions( "gb.a1" );
                pushOptions( "-p" );
            }
  
            else if( false
            || option.equals( "W" )
            || option.equals( "whole-optimize" )
            ) {
                
                pushOptions( "-O" );
                pushOptions( "-w" );
                pushOptions( "enabled:true" );
                pushOptions( "wsop" );
                pushOptions( "-p" );
                pushOptions( "enabled:true" );
                pushOptions( "wjop" );
                pushOptions( "-p" );
            }
  
            else if( false 
            || option.equals( "via-grimp" )
            )
                via_grimp = true;
  
            else if( false 
            || option.equals( "via-shimple" )
            )
                via_shimple = true;
  
            else if( false
            || option.equals( "throw-analysis" )
            ) {
                if( !hasMoreOptions() ) {
                    G.v().out.println( "No value given for option -"+option );
                    return false;
                }
                String value = nextOption();
    
                if( false );
    
                else if( false
                || value.equals( "pedantic" )
                ) {
                    if( throw_analysis != 0
                    && throw_analysis != throw_analysis_pedantic ) {
                        G.v().out.println( "Multiple values given for option "+option );
                        return false;
                    }
                    throw_analysis = throw_analysis_pedantic;
                }
    
                else if( false
                || value.equals( "unit" )
                ) {
                    if( throw_analysis != 0
                    && throw_analysis != throw_analysis_unit ) {
                        G.v().out.println( "Multiple values given for option "+option );
                        return false;
                    }
                    throw_analysis = throw_analysis_unit;
                }
    
                else {
                    G.v().out.println( "Invalid value "+value+" given for option -"+option );
                    return false;
                }
           }
  
            else if( false
            || option.equals( "check-init-ta" )
            || option.equals( "check-init-throw-analysis" )
            ) {
                if( !hasMoreOptions() ) {
                    G.v().out.println( "No value given for option -"+option );
                    return false;
                }
                String value = nextOption();
    
                if( false );
    
                else if( false
                || value.equals( "auto" )
                ) {
                    if( check_init_throw_analysis != 0
                    && check_init_throw_analysis != check_init_throw_analysis_auto ) {
                        G.v().out.println( "Multiple values given for option "+option );
                        return false;
                    }
                    check_init_throw_analysis = check_init_throw_analysis_auto;
                }
    
                else if( false
                || value.equals( "pedantic" )
                ) {
                    if( check_init_throw_analysis != 0
                    && check_init_throw_analysis != check_init_throw_analysis_pedantic ) {
                        G.v().out.println( "Multiple values given for option "+option );
                        return false;
                    }
                    check_init_throw_analysis = check_init_throw_analysis_pedantic;
                }
    
                else if( false
                || value.equals( "unit" )
                ) {
                    if( check_init_throw_analysis != 0
                    && check_init_throw_analysis != check_init_throw_analysis_unit ) {
                        G.v().out.println( "Multiple values given for option "+option );
                        return false;
                    }
                    check_init_throw_analysis = check_init_throw_analysis_unit;
                }
    
                else if( false
                || value.equals( "dalvik" )
                ) {
                    if( check_init_throw_analysis != 0
                    && check_init_throw_analysis != check_init_throw_analysis_dalvik ) {
                        G.v().out.println( "Multiple values given for option "+option );
                        return false;
                    }
                    check_init_throw_analysis = check_init_throw_analysis_dalvik;
                }
    
                else {
                    G.v().out.println( "Invalid value "+value+" given for option -"+option );
                    return false;
                }
           }
  
            else if( false 
            || option.equals( "omit-excepting-unit-edges" )
            )
                omit_excepting_unit_edges = true;
  
            else if( false
            || option.equals( "trim-cfgs" )
            ) {
                
                pushOptions( "enabled:true" );
                pushOptions( "jb.tt" );
                pushOptions( "-p" );
                pushOptions( "-omit-excepting-unit-edges" );
                pushOptions( "unit" );
                pushOptions( "-throw-analysis" );
            }
  
            else if( false 
            || option.equals( "ire" )
            || option.equals( "ignore-resolution-errors" )
            )
                ignore_resolution_errors = true;
  
            else if( false
            || option.equals( "i" )
            || option.equals( "include" )
            ) {
                if( !hasMoreOptions() ) {
                    G.v().out.println( "No value given for option -"+option );
                    return false;
                }
                String value = nextOption();
    
                if( include == null )
                    include = new LinkedList<String>();

                include.add( value );
                
            }
  
            else if( false
            || option.equals( "x" )
            || option.equals( "exclude" )
            ) {
                if( !hasMoreOptions() ) {
                    G.v().out.println( "No value given for option -"+option );
                    return false;
                }
                String value = nextOption();
    
                if( exclude == null )
                    exclude = new LinkedList<String>();

                exclude.add( value );
                
            }
  
            else if( false 
            || option.equals( "include-all" )
            )
                include_all = true;
  
            else if( false
            || option.equals( "dynamic-class" )
            ) {
                if( !hasMoreOptions() ) {
                    G.v().out.println( "No value given for option -"+option );
                    return false;
                }
                String value = nextOption();
    
                if( dynamic_class == null )
                    dynamic_class = new LinkedList<String>();

                dynamic_class.add( value );
                
            }
  
            else if( false
            || option.equals( "dynamic-dir" )
            ) {
                if( !hasMoreOptions() ) {
                    G.v().out.println( "No value given for option -"+option );
                    return false;
                }
                String value = nextOption();
    
                if( dynamic_dir == null )
                    dynamic_dir = new LinkedList<String>();

                dynamic_dir.add( value );
                
            }
  
            else if( false
            || option.equals( "dynamic-package" )
            ) {
                if( !hasMoreOptions() ) {
                    G.v().out.println( "No value given for option -"+option );
                    return false;
                }
                String value = nextOption();
    
                if( dynamic_package == null )
                    dynamic_package = new LinkedList<String>();

                dynamic_package.add( value );
                
            }
  
            else if( false 
            || option.equals( "keep-line-number" )
            )
                keep_line_number = true;
  
            else if( false 
            || option.equals( "keep-bytecode-offset" )
            || option.equals( "keep-offset" )
            )
                keep_offset = true;
  
            else if( false
            || option.equals( "annot-purity" )
            ) {
                
                pushOptions( "enabled:true" );
                pushOptions( "wjap.purity" );
                pushOptions( "-p" );
                pushOptions( "enabled:true" );
                pushOptions( "cg.spark" );
                pushOptions( "-p" );
                pushOptions( "-w" );
            }
  
            else if( false
            || option.equals( "annot-nullpointer" )
            ) {
                
                pushOptions( "enabled:true" );
                pushOptions( "tag.an" );
                pushOptions( "-p" );
                pushOptions( "enabled:true" );
                pushOptions( "jap.npc" );
                pushOptions( "-p" );
            }
  
            else if( false
            || option.equals( "annot-arraybounds" )
            ) {
                
                pushOptions( "enabled:true" );
                pushOptions( "tag.an" );
                pushOptions( "-p" );
                pushOptions( "enabled:true" );
                pushOptions( "jap.abc" );
                pushOptions( "-p" );
                pushOptions( "enabled:true" );
                pushOptions( "wjap.ra" );
                pushOptions( "-p" );
            }
  
            else if( false
            || option.equals( "annot-side-effect" )
            ) {
                
                pushOptions( "enabled:true" );
                pushOptions( "tag.dep" );
                pushOptions( "-p" );
                pushOptions( "enabled:true" );
                pushOptions( "jap.sea" );
                pushOptions( "-p" );
                pushOptions( "-w" );
            }
  
            else if( false
            || option.equals( "annot-fieldrw" )
            ) {
                
                pushOptions( "enabled:true" );
                pushOptions( "tag.fieldrw" );
                pushOptions( "-p" );
                pushOptions( "enabled:true" );
                pushOptions( "jap.fieldrw" );
                pushOptions( "-p" );
                pushOptions( "-w" );
            }
  
            else if( false 
            || option.equals( "time" )
            )
                time = true;
  
            else if( false 
            || option.equals( "subtract-gc" )
            )
                subtract_gc = true;
  
            else if( false 
            || option.equals( "no-writeout-body-releasing" )
            )
                no_writeout_body_releasing = true;
  
            else {
                G.v().out.println( "Invalid option -"+option );
                return false;
            }
        }

        Iterator<String> it = phaseOptions.iterator();
        while( it.hasNext() ) {
            String phaseName = it.next();
            String phaseOption = it.next();
            if( !setPhaseOption( phaseName, "enabled:true" ) ) return false;
        }

        it = phaseOptions.iterator();
        while( it.hasNext() ) {
            String phaseName = it.next();
            String phaseOption = it.next();
            if( !setPhaseOption( phaseName, phaseOption ) ) return false;
        }

        return true;
    }


    public boolean coffi() { return coffi; }
    private boolean coffi = false;
    public void set_coffi( boolean setting ) { coffi = setting; }
  
    public boolean help() { return help; }
    private boolean help = false;
    public void set_help( boolean setting ) { help = setting; }
  
    public boolean phase_list() { return phase_list; }
    private boolean phase_list = false;
    public void set_phase_list( boolean setting ) { phase_list = setting; }
  
    public List<String> phase_help() { 
        if( phase_help == null )
            return java.util.Collections.emptyList();
        else
            return phase_help;
    }
    public void set_phase_help( List<String> setting ) { phase_help = setting; }
    private List<String> phase_help = null;
    public boolean version() { return version; }
    private boolean version = false;
    public void set_version( boolean setting ) { version = setting; }
  
    public boolean verbose() { return verbose; }
    private boolean verbose = false;
    public void set_verbose( boolean setting ) { verbose = setting; }
  
    public boolean interactive_mode() { return interactive_mode; }
    private boolean interactive_mode = false;
    public void set_interactive_mode( boolean setting ) { interactive_mode = setting; }
  
    public boolean unfriendly_mode() { return unfriendly_mode; }
    private boolean unfriendly_mode = false;
    public void set_unfriendly_mode( boolean setting ) { unfriendly_mode = setting; }
  
    public boolean app() { return app; }
    private boolean app = false;
    public void set_app( boolean setting ) { app = setting; }
  
    public boolean whole_program() { return whole_program; }
    private boolean whole_program = false;
    public void set_whole_program( boolean setting ) { whole_program = setting; }
  
    public boolean whole_shimple() { return whole_shimple; }
    private boolean whole_shimple = false;
    public void set_whole_shimple( boolean setting ) { whole_shimple = setting; }
  
    public boolean on_the_fly() { return on_the_fly; }
    private boolean on_the_fly = false;
    public void set_on_the_fly( boolean setting ) { on_the_fly = setting; }
  
    public boolean validate() { return validate; }
    private boolean validate = false;
    public void set_validate( boolean setting ) { validate = setting; }
  
    public boolean debug() { return debug; }
    private boolean debug = false;
    public void set_debug( boolean setting ) { debug = setting; }
  
    public boolean debug_resolver() { return debug_resolver; }
    private boolean debug_resolver = false;
    public void set_debug_resolver( boolean setting ) { debug_resolver = setting; }
  
    public String soot_classpath() { return soot_classpath; }
    public void set_soot_classpath( String setting ) { soot_classpath = setting; }
    private String soot_classpath = "";
    public boolean prepend_classpath() { return prepend_classpath; }
    private boolean prepend_classpath = false;
    public void set_prepend_classpath( boolean setting ) { prepend_classpath = setting; }
  
    public List<String> process_dir() { 
        if( process_dir == null )
            return java.util.Collections.emptyList();
        else
            return process_dir;
    }
    public void set_process_dir( List<String> setting ) { process_dir = setting; }
    private List<String> process_dir = null;
    public boolean oaat() { return oaat; }
    private boolean oaat = false;
    public void set_oaat( boolean setting ) { oaat = setting; }
  
    public String android_jars() { return android_jars; }
    public void set_android_jars( String setting ) { android_jars = setting; }
    private String android_jars = "";
    public String force_android_jar() { return force_android_jar; }
    public void set_force_android_jar( String setting ) { force_android_jar = setting; }
    private String force_android_jar = "";
    public boolean ast_metrics() { return ast_metrics; }
    private boolean ast_metrics = false;
    public void set_ast_metrics( boolean setting ) { ast_metrics = setting; }
  
    public int src_prec() {
        if( src_prec == 0 ) return src_prec_class;
        return src_prec; 
    }
    public void set_src_prec( int setting ) { src_prec = setting; }
    private int src_prec = 0;
    public boolean full_resolver() { return full_resolver; }
    private boolean full_resolver = false;
    public void set_full_resolver( boolean setting ) { full_resolver = setting; }
  
    public boolean allow_phantom_refs() { return allow_phantom_refs; }
    private boolean allow_phantom_refs = false;
    public void set_allow_phantom_refs( boolean setting ) { allow_phantom_refs = setting; }
  
    public boolean no_bodies_for_excluded() { return no_bodies_for_excluded; }
    private boolean no_bodies_for_excluded = false;
    public void set_no_bodies_for_excluded( boolean setting ) { no_bodies_for_excluded = setting; }
  
    public boolean j2me() { return j2me; }
    private boolean j2me = false;
    public void set_j2me( boolean setting ) { j2me = setting; }
  
    public String main_class() { return main_class; }
    public void set_main_class( String setting ) { main_class = setting; }
    private String main_class = "";
    public boolean polyglot() { return polyglot; }
    private boolean polyglot = false;
    public void set_polyglot( boolean setting ) { polyglot = setting; }
  
    public String output_dir() { return output_dir; }
    public void set_output_dir( String setting ) { output_dir = setting; }
    private String output_dir = "";
    public int output_format() {
        if( output_format == 0 ) return output_format_class;
        return output_format; 
    }
    public void set_output_format( int setting ) { output_format = setting; }
    private int output_format = 0;
    public boolean output_jar() { return output_jar; }
    private boolean output_jar = false;
    public void set_output_jar( boolean setting ) { output_jar = setting; }
  
    public boolean xml_attributes() { return xml_attributes; }
    private boolean xml_attributes = false;
    public void set_xml_attributes( boolean setting ) { xml_attributes = setting; }
  
    public boolean print_tags_in_output() { return print_tags_in_output; }
    private boolean print_tags_in_output = false;
    public void set_print_tags_in_output( boolean setting ) { print_tags_in_output = setting; }
  
    public boolean no_output_source_file_attribute() { return no_output_source_file_attribute; }
    private boolean no_output_source_file_attribute = false;
    public void set_no_output_source_file_attribute( boolean setting ) { no_output_source_file_attribute = setting; }
  
    public boolean no_output_inner_classes_attribute() { return no_output_inner_classes_attribute; }
    private boolean no_output_inner_classes_attribute = false;
    public void set_no_output_inner_classes_attribute( boolean setting ) { no_output_inner_classes_attribute = setting; }
  
    public List<String> dump_body() { 
        if( dump_body == null )
            return java.util.Collections.emptyList();
        else
            return dump_body;
    }
    public void set_dump_body( List<String> setting ) { dump_body = setting; }
    private List<String> dump_body = null;
    public List<String> dump_cfg() { 
        if( dump_cfg == null )
            return java.util.Collections.emptyList();
        else
            return dump_cfg;
    }
    public void set_dump_cfg( List<String> setting ) { dump_cfg = setting; }
    private List<String> dump_cfg = null;
    public boolean show_exception_dests() { return show_exception_dests; }
    private boolean show_exception_dests = false;
    public void set_show_exception_dests( boolean setting ) { show_exception_dests = setting; }
  
    public boolean gzip() { return gzip; }
    private boolean gzip = false;
    public void set_gzip( boolean setting ) { gzip = setting; }
  
    public boolean force_overwrite() { return force_overwrite; }
    private boolean force_overwrite = false;
    public void set_force_overwrite( boolean setting ) { force_overwrite = setting; }
  
    public List<String> plugin() { 
        if( plugin == null )
            return java.util.Collections.emptyList();
        else
            return plugin;
    }
    public void set_plugin( List<String> setting ) { plugin = setting; }
    private List<String> plugin = null;
    public boolean via_grimp() { return via_grimp; }
    private boolean via_grimp = false;
    public void set_via_grimp( boolean setting ) { via_grimp = setting; }
  
    public boolean via_shimple() { return via_shimple; }
    private boolean via_shimple = false;
    public void set_via_shimple( boolean setting ) { via_shimple = setting; }
  
    public int throw_analysis() {
        if( throw_analysis == 0 ) return throw_analysis_unit;
        return throw_analysis; 
    }
    public void set_throw_analysis( int setting ) { throw_analysis = setting; }
    private int throw_analysis = 0;
    public int check_init_throw_analysis() {
        if( check_init_throw_analysis == 0 ) return check_init_throw_analysis_auto;
        return check_init_throw_analysis; 
    }
    public void set_check_init_throw_analysis( int setting ) { check_init_throw_analysis = setting; }
    private int check_init_throw_analysis = 0;
    public boolean omit_excepting_unit_edges() { return omit_excepting_unit_edges; }
    private boolean omit_excepting_unit_edges = false;
    public void set_omit_excepting_unit_edges( boolean setting ) { omit_excepting_unit_edges = setting; }
  
    public boolean ignore_resolution_errors() { return ignore_resolution_errors; }
    private boolean ignore_resolution_errors = false;
    public void set_ignore_resolution_errors( boolean setting ) { ignore_resolution_errors = setting; }
  
    public List<String> include() { 
        if( include == null )
            return java.util.Collections.emptyList();
        else
            return include;
    }
    public void set_include( List<String> setting ) { include = setting; }
    private List<String> include = null;
    public List<String> exclude() { 
        if( exclude == null )
            return java.util.Collections.emptyList();
        else
            return exclude;
    }
    public void set_exclude( List<String> setting ) { exclude = setting; }
    private List<String> exclude = null;
    public boolean include_all() { return include_all; }
    private boolean include_all = false;
    public void set_include_all( boolean setting ) { include_all = setting; }
  
    public List<String> dynamic_class() { 
        if( dynamic_class == null )
            return java.util.Collections.emptyList();
        else
            return dynamic_class;
    }
    public void set_dynamic_class( List<String> setting ) { dynamic_class = setting; }
    private List<String> dynamic_class = null;
    public List<String> dynamic_dir() { 
        if( dynamic_dir == null )
            return java.util.Collections.emptyList();
        else
            return dynamic_dir;
    }
    public void set_dynamic_dir( List<String> setting ) { dynamic_dir = setting; }
    private List<String> dynamic_dir = null;
    public List<String> dynamic_package() { 
        if( dynamic_package == null )
            return java.util.Collections.emptyList();
        else
            return dynamic_package;
    }
    public void set_dynamic_package( List<String> setting ) { dynamic_package = setting; }
    private List<String> dynamic_package = null;
    public boolean keep_line_number() { return keep_line_number; }
    private boolean keep_line_number = false;
    public void set_keep_line_number( boolean setting ) { keep_line_number = setting; }
  
    public boolean keep_offset() { return keep_offset; }
    private boolean keep_offset = false;
    public void set_keep_offset( boolean setting ) { keep_offset = setting; }
  
    public boolean time() { return time; }
    private boolean time = false;
    public void set_time( boolean setting ) { time = setting; }
  
    public boolean subtract_gc() { return subtract_gc; }
    private boolean subtract_gc = false;
    public void set_subtract_gc( boolean setting ) { subtract_gc = setting; }
  
    public boolean no_writeout_body_releasing() { return no_writeout_body_releasing; }
    private boolean no_writeout_body_releasing = false;
    public void set_no_writeout_body_releasing( boolean setting ) { no_writeout_body_releasing = setting; }
  

    public String getUsage() {
        return ""

+"\nGeneral Options:\n"
      
+padOpt(" -coffi", "Use the good old Coffi front end for parsing Java bytecode (instead of using ASM)." )
+padOpt(" -h -help", "Display help and exit" )
+padOpt(" -pl -phase-list", "Print list of available phases" )
+padOpt(" -ph PHASE -phase-help PHASE", "Print help for specified PHASE" )
+padOpt(" -version", "Display version information and exit" )
+padOpt(" -v -verbose", "Verbose mode" )
+padOpt(" -interactive-mode", "Run in interactive mode" )
+padOpt(" -unfriendly-mode", "Allow Soot to run with no command-line options" )
+padOpt(" -app", "Run in application mode" )
+padOpt(" -w -whole-program", "Run in whole-program mode" )
+padOpt(" -ws -whole-shimple", "Run in whole-shimple mode" )
+padOpt(" -fly -on-the-fly", "Run in on-the-fly mode" )
+padOpt(" -validate", "Run internal validation on bodies" )
+padOpt(" -debug", "Print various Soot debugging info" )
+padOpt(" -debug-resolver", "Print debugging info from SootResolver" )
+"\nInput Options:\n"
      
+padOpt(" -cp PATH -soot-class-path PATH -soot-classpath PATH", "Use PATH as the classpath for finding classes." )
+padOpt(" -pp -prepend-classpath", "Prepend the given soot classpath to the default classpath." )
+padOpt(" -process-path DIR -process-dir DIR", "Process all classes found in DIR" )
+padOpt(" -oaat", "From the process-dir, processes one class at a time." )
+padOpt(" -android-jars PATH", "Use PATH as the path for finding the android.jar file" )
+padOpt(" -force-android-jar PATH", "Force Soot to use PATH as the path for the android.jar file." )
+padOpt(" -ast-metrics", "Compute AST Metrics if performing java to jimple" )
+padOpt(" -src-prec FORMAT", "Sets source precedence to FORMAT files" )
+padVal(" c class (default)", "Favour class files as Soot source" )
+padVal(" only-class", "Use only class files as Soot source" )
+padVal(" J jimple", "Favour Jimple files as Soot source" )
+padVal(" java", "Favour Java files as Soot source" )
+padVal(" apk", "Favour APK files as Soot source" )
+padOpt(" -full-resolver", "Force transitive resolving of referenced classes" )
+padOpt(" -allow-phantom-refs", "Allow unresolved classes; may cause errors" )
+padOpt(" -no-bodies-for-excluded", "Do not load bodies for excluded classes" )
+padOpt(" -j2me", "Use J2ME mode; changes assignment of types" )
+padOpt(" -main-class CLASS", "Sets the main class for whole-program analysis." )
+padOpt(" -polyglot", "Use Java 1.4 Polyglot frontend instead of JastAdd" )
+"\nOutput Options:\n"
      
+padOpt(" -d DIR -output-dir DIR", "Store output files in DIR" )
+padOpt(" -f FORMAT -output-format FORMAT", "Set output format for Soot" )
+padVal(" J jimple", "Produce .jimple Files" )
+padVal(" j jimp", "Produce .jimp (abbreviated Jimple) files" )
+padVal(" S shimple", "Produce .shimple files" )
+padVal(" s shimp", "Produce .shimp (abbreviated Shimple) files" )
+padVal(" B baf", "Produce .baf files" )
+padVal(" b", "Produce .b (abbreviated Baf) files" )
+padVal(" G grimple", "Produce .grimple files" )
+padVal(" g grimp", "Produce .grimp (abbreviated Grimp) files" )
+padVal(" X xml", "Produce .xml Files" )
+padVal(" dex", "Produce Dalvik Virtual Machine files" )
+padVal(" n none", "Produce no output" )
+padVal(" jasmin", "Produce .jasmin files" )
+padVal(" c class (default)", "Produce .class Files" )
+padVal(" d dava", "Produce dava-decompiled .java files" )
+padVal(" t template", "Produce .java files with Jimple templates." )
+padOpt(" -outjar -output-jar", "Make output dir a Jar file instead of dir" )
+padOpt(" -xml-attributes", "Save tags to XML attributes for Eclipse" )
+padOpt(" -print-tags -print-tags-in-output", "Print tags in output files after stmt" )
+padOpt(" -no-output-source-file-attribute", "Don't output Source File Attribute when producing class files" )
+padOpt(" -no-output-inner-classes-attribute", "Don't output inner classes attribute in class files" )
+padOpt(" -dump-body PHASENAME", "Dump the internal representation of each method before and after phase PHASENAME" )
+padOpt(" -dump-cfg PHASENAME", "Dump the internal representation of each CFG constructed during phase PHASENAME" )
+padOpt(" -show-exception-dests", "Include exception destination edges as well as CFG edges in dumped CFGs" )
+padOpt(" -gzip", "GZip IR output files" )
+padOpt(" -force-overwrite", "Force Overwrite Output Files" )
+"\nProcessing Options:\n"
      
+padOpt(" -plugin FILE", "Load all plugins found in FILE" )
+padOpt(" -p PHASE OPT:VAL -phase-option PHASE OPT:VAL", "Set PHASE's OPT option to VALUE" )
+padOpt(" -O -optimize", "Perform intraprocedural optimizations" )
+padOpt(" -W -whole-optimize", "Perform whole program optimizations" )
+padOpt(" -via-grimp", "Convert to bytecode via Grimp instead of via Baf" )
+padOpt(" -via-shimple", "Enable Shimple SSA representation" )
+padOpt(" -throw-analysis ARG", "" )
+padVal(" pedantic", "Pedantically conservative throw analysis" )
+padVal(" unit (default)", "Unit Throw Analysis" )
+padOpt(" -check-init-ta ARG -check-init-throw-analysis ARG", "" )
+padVal(" auto (default)", "Automatically select a throw analysis" )
+padVal(" pedantic", "Pedantically conservative throw analysis" )
+padVal(" unit", "Unit Throw Analysis" )
+padVal(" dalvik", "Dalvik Throw Analysis" )
+padOpt(" -omit-excepting-unit-edges", "Omit CFG edges to handlers from excepting units which lack side effects" )
+padOpt(" -trim-cfgs", "Trim unrealizable exceptional edges from CFGs" )
+padOpt(" -ire -ignore-resolution-errors", "Does not throw an exception when a program references an undeclared field or method." )
+"\nApplication Mode Options:\n"
      
+padOpt(" -i PKG -include PKG", "Include classes in PKG as application classes" )
+padOpt(" -x PKG -exclude PKG", "Exclude classes in PKG from application classes" )
+padOpt(" -include-all", "Set default excluded packages to empty list" )
+padOpt(" -dynamic-class CLASS", "Note that CLASS may be loaded dynamically" )
+padOpt(" -dynamic-dir DIR", "Mark all classes in DIR as potentially dynamic" )
+padOpt(" -dynamic-package PKG", "Marks classes in PKG as potentially dynamic" )
+"\nInput Attribute Options:\n"
      
+padOpt(" -keep-line-number", "Keep line number tables" )
+padOpt(" -keep-bytecode-offset -keep-offset", "Attach bytecode offset to IR" )
+"\nAnnotation Options:\n"
      
+padOpt(" -annot-purity", "Emit purity attributes" )
+padOpt(" -annot-nullpointer", "Emit null pointer attributes" )
+padOpt(" -annot-arraybounds", "Emit array bounds check attributes" )
+padOpt(" -annot-side-effect", "Emit side-effect attributes" )
+padOpt(" -annot-fieldrw", "Emit field read/write attributes" )
+"\nMiscellaneous Options:\n"
      
+padOpt(" -time", "Report time required for transformations" )
+padOpt(" -subtract-gc", "Subtract gc from time" )
+padOpt(" -no-writeout-body-releasing", "Disables the release of method bodies after writeout. This flag is used internally." );
    }


    public String getPhaseList() {
        return ""
    
        +padOpt("jb", "Creates a JimpleBody for each method")
        +padVal("jb.ls", "Local splitter: one local per DU-UD web")
        +padVal("jb.a", "Aggregator: removes some unnecessary copies")
        +padVal("jb.ule", "Unused local eliminator")
        +padVal("jb.tr", "Assigns types to locals")
        +padVal("jb.ulp", "Local packer: minimizes number of locals")
        +padVal("jb.lns", "Local name standardizer")
        +padVal("jb.cp", "Copy propagator")
        +padVal("jb.dae", "Dead assignment eliminator")
        +padVal("jb.cp-ule", "Post-copy propagation unused local eliminator")
        +padVal("jb.lp", "Local packer: minimizes number of locals")
        +padVal("jb.ne", "Nop eliminator")
        +padVal("jb.uce", "Unreachable code eliminator")
        +padVal("jb.tt", "Trap Tightener")
        +padOpt("jj", "Creates a JimpleBody for each method directly from source")
        +padVal("jj.ls", "Local splitter: one local per DU-UD web")
        +padVal("jj.a", "Aggregator: removes some unnecessary copies")
        +padVal("jj.ule", "Unused local eliminator")
        +padVal("jj.tr", "Assigns types to locals")
        +padVal("jj.ulp", "Local packer: minimizes number of locals")
        +padVal("jj.lns", "Local name standardizer")
        +padVal("jj.cp", "Copy propagator")
        +padVal("jj.dae", "Dead assignment eliminator")
        +padVal("jj.cp-ule", "Post-copy propagation unused local eliminator")
        +padVal("jj.lp", "Local packer: minimizes number of locals")
        +padVal("jj.ne", "Nop eliminator")
        +padVal("jj.uce", "Unreachable code eliminator")
        +padOpt("wjpp", "Whole Jimple Pre-processing Pack")
        +padOpt("wspp", "Whole Shimple Pre-processing Pack")
        +padOpt("cg", "Call graph constructor")
        +padVal("cg.cha", "Builds call graph using Class Hierarchy Analysis")
        +padVal("cg.spark", "Spark points-to analysis framework")
        +padVal("cg.paddle", "Paddle points-to analysis framework")
        +padOpt("wstp", "Whole-shimple transformation pack")
        +padOpt("wsop", "Whole-shimple optimization pack")
        +padOpt("wjtp", "Whole-jimple transformation pack")
        +padVal("wjtp.mhp", "Determines what statements may be run concurrently")
        +padVal("wjtp.tn", "Finds critical sections, allocates locks")
        +padVal("wjtp.rdc", "Rename duplicated classes when the file system is not case sensitive")
        +padOpt("wjop", "Whole-jimple optimization pack")
        +padVal("wjop.smb", "Static method binder: Devirtualizes monomorphic calls")
        +padVal("wjop.si", "Static inliner: inlines monomorphic calls")
        +padOpt("wjap", "Whole-jimple annotation pack: adds interprocedural tags")
        +padVal("wjap.ra", "Rectangular array finder")
        +padVal("wjap.umt", "Tags all unreachable methods")
        +padVal("wjap.uft", "Tags all unreachable fields")
        +padVal("wjap.tqt", "Tags all qualifiers that could be tighter")
        +padVal("wjap.cgg", "Creates graphical call graph.")
        +padVal("wjap.purity", "Emit purity attributes")
        +padOpt("shimple", "Sets parameters for Shimple SSA form")
        +padOpt("stp", "Shimple transformation pack")
        +padOpt("sop", "Shimple optimization pack")
        +padVal("sop.cpf", "Shimple constant propagator and folder")
        +padOpt("jtp", "Jimple transformation pack: intraprocedural analyses added to Soot")
        +padOpt("jop", "Jimple optimization pack (intraprocedural)")
        +padVal("jop.cse", "Common subexpression eliminator")
        +padVal("jop.bcm", "Busy code motion: unaggressive partial redundancy elimination")
        +padVal("jop.lcm", "Lazy code motion: aggressive partial redundancy elimination")
        +padVal("jop.cp", "Copy propagator")
        +padVal("jop.cpf", "Constant propagator and folder")
        +padVal("jop.cbf", "Conditional branch folder")
        +padVal("jop.dae", "Dead assignment eliminator")
        +padVal("jop.nce", "Null Check Eliminator")
        +padVal("jop.uce1", "Unreachable code eliminator, pass 1")
        +padVal("jop.ubf1", "Unconditional branch folder, pass 1")
        +padVal("jop.uce2", "Unreachable code eliminator, pass 2")
        +padVal("jop.ubf2", "Unconditional branch folder, pass 2")
        +padVal("jop.ule", "Unused local eliminator")
        +padOpt("jap", "Jimple annotation pack: adds intraprocedural tags")
        +padVal("jap.npc", "Null pointer checker")
        +padVal("jap.npcolorer", "Null pointer colourer: tags references for eclipse")
        +padVal("jap.abc", "Array bound checker")
        +padVal("jap.profiling", "Instruments null pointer and array checks")
        +padVal("jap.sea", "Side effect tagger")
        +padVal("jap.fieldrw", "Field read/write tagger")
        +padVal("jap.cgtagger", "Call graph tagger")
        +padVal("jap.parity", "Parity tagger")
        +padVal("jap.pat", "Colour-codes method parameters that may be aliased")
        +padVal("jap.lvtagger", "Creates color tags for live variables")
        +padVal("jap.rdtagger", "Creates link tags for reaching defs")
        +padVal("jap.che", "Indicates whether cast checks can be eliminated")
        +padVal("jap.umt", "Inserts assertions into unreachable methods")
        +padVal("jap.lit", "Tags loop invariants")
        +padVal("jap.aet", "Tags statements with sets of available expressions")
        +padVal("jap.dmt", "Tags dominators of statement")
        +padOpt("gb", "Creates a GrimpBody for each method")
        +padVal("gb.a1", "Aggregator: removes some copies, pre-folding")
        +padVal("gb.cf", "Constructor folder")
        +padVal("gb.a2", "Aggregator: removes some copies, post-folding")
        +padVal("gb.ule", "Unused local eliminator")
        +padOpt("gop", "Grimp optimization pack")
        +padOpt("bb", "Creates Baf bodies")
        +padVal("bb.lso", "Load store optimizer")
        +padVal("bb.pho", "Peephole optimizer")
        +padVal("bb.ule", "Unused local eliminator")
        +padVal("bb.lp", "Local packer: minimizes number of locals")
        +padOpt("bop", "Baf optimization pack")
        +padOpt("tag", "Tag aggregator: turns tags into attributes")
        +padVal("tag.ln", "Line number aggregator")
        +padVal("tag.an", "Array bounds and null pointer check aggregator")
        +padVal("tag.dep", "Dependence aggregator")
        +padVal("tag.fieldrw", "Field read/write aggregator")
        +padOpt("db", "Dummy phase to store options for Dava")
        +padVal("db.transformations", "The Dava back-end with all its transformations")
        +padVal("db.renamer", "Apply heuristics based naming of local variables")
        +padVal("db.deobfuscate", " Apply de-obfuscation analyses")
        +padVal("db.force-recompile", " Try to get recompilable code.");
    }

    public String getPhaseHelp( String phaseName ) {
    
        if( phaseName.equals( "jb" ) )
            return "Phase "+phaseName+":\n"+
                "\nJimple Body Creation creates a JimpleBody for each \ninput method, using either coffi, to read .class \nfiles, or the jimple parser, to read .jimple files. \n"
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" )
                +padOpt( "use-original-names (false)", "" )
                +padOpt( "preserve-source-annotations (false)", "" );
    
        if( phaseName.equals( "jb.ls" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Local Splitter identifies DU-UD webs for local \nvariables and introduces new variables so that each \ndisjoint web is associated with a single local. \n"
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" );
    
        if( phaseName.equals( "jb.a" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Jimple Local Aggregator removes some \nunnecessary copies by combining local variables. \nEssentially, it finds definitions which have only \na single use and, if it is safe to do so, removes \nthe original definition after replacing the use with the \ndefinition's right-hand side. At this stage in \nJimpleBody construction, local aggregation serves \nlargely to remove the copies to and from stack \nvariables which simulate load and store instructions in the \noriginal bytecode."
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" )
                +padOpt( "only-stack-locals (true)", "" );
    
        if( phaseName.equals( "jb.ule" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Unused Local Eliminator removes any unused \nlocals from the method. "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" );
    
        if( phaseName.equals( "jb.tr" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Type Assigner gives local variables types which \nwill accommodate the values stored in them over the \ncourse of the method. "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" )
                +padOpt( "ignore-wrong-staticness (false)", "Ignores errors due to wrong staticness" )
                +padOpt( "use-older-type-assigner (false)", "Enables the older type assigner" )
                +padOpt( "compare-type-assigners (false)", "Compares Ben Bellamy's and the older type assigner" );
    
        if( phaseName.equals( "jb.ulp" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Unsplit-originals Local Packer executes only \nwhen the `use-original-names' option is chosen for \nthe `jb' phase. The Local Packer attempts to \nminimize the number of local variables required in a \nmethod by reusing the same variable for disjoint \nDU-UD webs. Conceptually, it is the inverse of the \nLocal Splitter. "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" )
                +padOpt( "unsplit-original-locals (true)", "" );
    
        if( phaseName.equals( "jb.lns" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Local Name Standardizer assigns generic names \nto local variables. "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" )
                +padOpt( "only-stack-locals (false)", "" );
    
        if( phaseName.equals( "jb.cp" ) )
            return "Phase "+phaseName+":\n"+
                "\nThis phase performs cascaded copy propagation. \nIf the propagator encounters situations of \nthe form: A: a = ...; \n... B: x = a; ... C: \n... = ... x; where a and x are each \ndefined only once (at A and B, respectively), then \nit can propagate immediately without checking \nbetween B and C for redefinitions of a. In \nthis case the propagator is global. \nOtherwise, if a has multiple definitions then the \npropagator checks for redefinitions and propagates copies \nonly within extended basic blocks. "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" )
                +padOpt( "only-regular-locals (false)", "" )
                +padOpt( "only-stack-locals (true)", "" );
    
        if( phaseName.equals( "jb.dae" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Dead Assignment Eliminator eliminates \nassignment statements to locals whose values are not \nsubsequently used, unless evaluating the right-hand \nside of the assignment may cause side-effects. \n"
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" )
                +padOpt( "only-stack-locals (true)", "" );
    
        if( phaseName.equals( "jb.cp-ule" ) )
            return "Phase "+phaseName+":\n"+
                "\nThis phase removes any locals that are unused after \ncopy propagation. "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" );
    
        if( phaseName.equals( "jb.lp" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Local Packer attempts to minimize the number of \nlocal variables required in a method by reusing the \nsame variable for disjoint DU-UD webs. Conceptually, \nit is the inverse of the Local Splitter. "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (false)", "" )
                +padOpt( "unsplit-original-locals (false)", "" );
    
        if( phaseName.equals( "jb.ne" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Nop Eliminator removes nop statements from the \nmethod. "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" );
    
        if( phaseName.equals( "jb.uce" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Unreachable Code Eliminator removes unreachable \ncode and traps whose catch blocks are empty. \n"
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" )
                +padOpt( "remove-unreachable-traps (false)", "" );
    
        if( phaseName.equals( "jb.tt" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Trap Tightener changes the area protected by \neach exception handler, so that it begins with the \nfirst instruction in the old protected area which is \nactually capable of throwing an exception caught by the \nhandler, and ends just after the last instruction in the old \nprotected area which can throw an exception caught by \nthe handler. This reduces the chance of producing \nunverifiable code as a byproduct of pruning \nexceptional control flow within CFGs. "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (false)", "" );
    
        if( phaseName.equals( "jj" ) )
            return "Phase "+phaseName+":\n"+
                "\nJimple Body Creation creates a JimpleBody for each \ninput method, using polyglot, to read .java files. \n"
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" )
                +padOpt( "use-original-names (true)", "" );
    
        if( phaseName.equals( "jj.ls" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Local Splitter identifies DU-UD webs for local \nvariables and introduces new variables so that each \ndisjoint web is associated with a single local. \n"
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (false)", "" );
    
        if( phaseName.equals( "jj.a" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Jimple Local Aggregator removes some \nunnecessary copies by combining local variables. \nEssentially, it finds definitions which have only \na single use and, if it is safe to do so, removes \nthe original definition after replacing the use with the \ndefinition's right-hand side. At this stage in \nJimpleBody construction, local aggregation serves \nlargely to remove the copies to and from stack \nvariables which simulate load and store instructions in the \noriginal bytecode."
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" )
                +padOpt( "only-stack-locals (true)", "" );
    
        if( phaseName.equals( "jj.ule" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Unused Local Eliminator removes any unused \nlocals from the method. "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" );
    
        if( phaseName.equals( "jj.tr" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Type Assigner gives local variables types which \nwill accommodate the values stored in them over the \ncourse of the method. "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (false)", "" );
    
        if( phaseName.equals( "jj.ulp" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Unsplit-originals Local Packer executes only \nwhen the `use-original-names' option is chosen for \nthe `jb' phase. The Local Packer attempts to \nminimize the number of local variables required in a \nmethod by reusing the same variable for disjoint \nDU-UD webs. Conceptually, it is the inverse of the \nLocal Splitter. "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (false)", "" )
                +padOpt( "unsplit-original-locals (false)", "" );
    
        if( phaseName.equals( "jj.lns" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Local Name Standardizer assigns generic names \nto local variables. "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" )
                +padOpt( "only-stack-locals (false)", "" );
    
        if( phaseName.equals( "jj.cp" ) )
            return "Phase "+phaseName+":\n"+
                "\nThis phase performs cascaded copy propagation. \nIf the propagator encounters situations of \nthe form: A: a = ...; \n... B: x = a; ... C: \n... = ... x; where a and x are each \ndefined only once (at A and B, respectively), then \nit can propagate immediately without checking \nbetween B and C for redefinitions of a. In \nthis case the propagator is global. \nOtherwise, if a has multiple definitions then the \npropagator checks for redefinitions and propagates copies \nonly within extended basic blocks. "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" )
                +padOpt( "only-regular-locals (false)", "" )
                +padOpt( "only-stack-locals (true)", "" );
    
        if( phaseName.equals( "jj.dae" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Dead Assignment Eliminator eliminates \nassignment statements to locals whose values are not \nsubsequently used, unless evaluating the right-hand \nside of the assignment may cause side-effects. \n"
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" )
                +padOpt( "only-stack-locals (true)", "" );
    
        if( phaseName.equals( "jj.cp-ule" ) )
            return "Phase "+phaseName+":\n"+
                "\nThis phase removes any locals that are unused after \ncopy propagation. "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" );
    
        if( phaseName.equals( "jj.lp" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Local Packer attempts to minimize the number of \nlocal variables required in a method by reusing the \nsame variable for disjoint DU-UD webs. Conceptually, \nit is the inverse of the Local Splitter. "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (false)", "" )
                +padOpt( "unsplit-original-locals (false)", "" );
    
        if( phaseName.equals( "jj.ne" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Nop Eliminator removes nop statements from the \nmethod. "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" );
    
        if( phaseName.equals( "jj.uce" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Unreachable Code Eliminator removes unreachable \ncode and traps whose catch blocks are empty. \n"
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" );
    
        if( phaseName.equals( "wjpp" ) )
            return "Phase "+phaseName+":\n"+
                "\nThis pack allows you to insert pre-processors that \nare run before call-graph construction. Only enabled \nin whole-program mode. In an unmodified copy of Soot, \nthis pack is empty."
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" );
    
        if( phaseName.equals( "wspp" ) )
            return "Phase "+phaseName+":\n"+
                "\nThis pack allows you to insert pre-processors that \nare run before call-graph construction. Only enabled \nin whole-program Shimple mode. In an unmodified copy \nof Soot, this pack is empty."
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" );
    
        if( phaseName.equals( "cg" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Call Graph Constructor computes a call graph for \nwhole program analysis. When this pack finishes, a \ncall graph is available in the Scene. The different \nphases in this pack are different ways to construct \nthe call graph. Exactly one phase in this pack must be \nenabled; Soot will raise an error otherwise. "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" )
                +padOpt( "safe-forname (false)", "Handle Class.forName() calls conservatively" )
                +padOpt( "safe-newinstance (false)", "Handle Class.newInstance() calls conservatively" )
                +padOpt( "verbose (false)", "Print warnings about where the call graph may be incomplete" )
                +padOpt( "jdkver (3)", "JDK version for native methods" )
                +padOpt( "all-reachable (false)", "Assume all methods of application classes are reachable." )
                +padOpt( "implicit-entry (true)", "Include methods called implicitly by the VM as entry points" )
                +padOpt( "trim-clinit (true)", "Removes redundant static initializer calls" )
                +padOpt( "reflection-log", "Uses a reflection log to resolve reflective calls." )
                +padOpt( "guards (ignore)", "Describes how to guard the program from unsound assumptions." );
    
        if( phaseName.equals( "cg.cha" ) )
            return "Phase "+phaseName+":\n"+
                "\nThis phase uses Class Hierarchy Analysis to generate a call \ngraph."
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" )
                +padOpt( "verbose (false)", "Print statistics about the resulting call graph" );
    
        if( phaseName.equals( "cg.spark" ) )
            return "Phase "+phaseName+":\n"+
                "\nSpark is a flexible points-to analysis framework. Aside from \nbuilding a call graph, it also generates information about the \ntargets of pointers. For details about Spark, please see Ondrej \nLhotak's M.Sc. thesis."
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (false)", "" )
                +padOpt( "verbose (false)", "Print detailed information about the execution of Spark" )
                +padOpt( "ignore-types (false)", "Make Spark completely ignore declared types of variables" )
                +padOpt( "force-gc (false)", "Force garbage collection for measuring memory usage" )
                +padOpt( "pre-jimplify (false)", "Jimplify all methods before starting Spark" )
                +padOpt( "vta (false)", "Emulate Variable Type Analysis" )
                +padOpt( "rta (false)", "Emulate Rapid Type Analysis" )
                +padOpt( "field-based (false)", "Use a field-based rather than field-sensitive representation" )
                +padOpt( "types-for-sites (false)", "Represent objects by their actual type rather than allocation site" )
                +padOpt( "merge-stringbuffer (true)", "Represent all StringBuffers as one object" )
                +padOpt( "string-constants (false)", "Propagate all string constants, not just class names" )
                +padOpt( "simulate-natives (true)", "Simulate effects of native methods in standard class library" )
                +padOpt( "empties-as-allocs (false)", "Treat singletons for empty sets etc. as allocation sites" )
                +padOpt( "simple-edges-bidirectional (false)", "Equality-based analysis between variable nodes" )
                +padOpt( "on-fly-cg (true)", "Build call graph as receiver types become known" )
                +padOpt( "simplify-offline (false)", "Collapse single-entry subgraphs of the PAG" )
                +padOpt( "simplify-sccs (false)", "Collapse strongly-connected components of the PAG" )
                +padOpt( "ignore-types-for-sccs (false)", "Ignore declared types when determining node equivalence for SCCs" )
                +padOpt( "propagator", "Select propagation algorithm" )
                +padVal( "iter", "Simple iterative algorithm" )
                
                +padVal( "worklist (default)", "Fast, worklist-based algorithm" )
                
                +padVal( "cycle", "Unfinished on-the-fly cycle detection algorithm" )
                
                +padVal( "merge", "Unfinished field reference merging algorithms" )
                
                +padVal( "alias", "Alias-edge based algorithm" )
                
                +padVal( "none", "Disable propagation" )
                
                +padOpt( "set-impl", "Select points-to set implementation" )
                +padVal( "hash", "Use Java HashSet" )
                
                +padVal( "bit", "Bit vector" )
                
                +padVal( "hybrid", "Hybrid representation using bit vector for large sets" )
                
                +padVal( "array", "Sorted array representation" )
                
                +padVal( "heintze", "Heintze's shared bit-vector and overflow list representation" )
                
                +padVal( "sharedlist", "Shared list representation" )
                
                +padVal( "double (default)", "Double set representation for incremental propagation" )
                
                +padOpt( "double-set-old", "Select implementation of points-to set for old part of double set" )
                +padVal( "hash", "Use Java HashSet" )
                
                +padVal( "bit", "Bit vector" )
                
                +padVal( "hybrid (default)", "Hybrid representation using bit vector for large sets" )
                
                +padVal( "array", "Sorted array representation" )
                
                +padVal( "heintze", "Heintze's shared bit-vector and overflow list representation" )
                
                +padVal( "sharedlist", "Shared list representation" )
                
                +padOpt( "double-set-new", "Select implementation of points-to set for new part of double set" )
                +padVal( "hash", "Use Java HashSet" )
                
                +padVal( "bit", "Bit vector" )
                
                +padVal( "hybrid (default)", "Hybrid representation using bit vector for large sets" )
                
                +padVal( "array", "Sorted array representation" )
                
                +padVal( "heintze", "Heintze's shared bit-vector and overflow list representation" )
                
                +padVal( "sharedlist", "Shared list representation" )
                
                +padOpt( "dump-html (false)", "Dump pointer assignment graph to HTML for debugging" )
                +padOpt( "dump-pag (false)", "Dump pointer assignment graph for other solvers" )
                +padOpt( "dump-solution (false)", "Dump final solution for comparison with other solvers" )
                +padOpt( "topo-sort (false)", "Sort variable nodes in dump" )
                +padOpt( "dump-types (true)", "Include declared types in dump" )
                +padOpt( "class-method-var (true)", "In dump, label variables by class and method" )
                +padOpt( "dump-answer (false)", "Dump computed reaching types for comparison with other solvers" )
                +padOpt( "add-tags (false)", "Output points-to results in tags for viewing with the Jimple" )
                +padOpt( "set-mass (false)", "Calculate statistics about points-to set sizes" )
                +padOpt( "cs-demand (false)", "After running Spark, refine points-to sets on demand with context information" )
                +padOpt( "lazy-pts (true)", "Create lazy points-to sets that create context information only when needed." )
                +padOpt( "traversal (75000)", "Make the analysis traverse at most this number of nodes per query." )
                +padOpt( "passes (10)", "Perform at most this number of refinement iterations." )
                +padOpt( "geom-pta (false)", "This switch enables/disables the geometric analysis." )
                +padOpt( "geom-encoding (Geom)", "Encoding methodology" )
                +padVal( "Geom (default)", "Geometric Encoding" )
                
                +padVal( "HeapIns", "Heap Insensitive Encoding" )
                
                +padVal( "PtIns", "PtIns" )
                
                +padOpt( "geom-worklist (PQ)", "Worklist type" )
                +padVal( "PQ (default)", "Priority Queue" )
                
                +padVal( "FIFO", "FIFO Queue" )
                
                +padOpt( "geom-dump-verbose ()", "Filename for detailed execution log" )
                +padOpt( "geom-verify-name ()", "Filename for verification file" )
                +padOpt( "geom-eval (0)", "Precision evaluation methodologies" )
                +padOpt( "geom-trans (false)", "Transform to context-insensitive result" )
                +padOpt( "geom-frac-base (40)", "Fractional parameter for precision/performance trade-off" )
                +padOpt( "geom-blocking (true)", "Enable blocking strategy for recursive calls" )
                +padOpt( "geom-runs (1)", "Iterations of analysis" )
                +padOpt( "kobjsens (0)", "Run object sensitivity with k." )
                +padOpt( "kobjsens-context-for-static-inits (false)", "Keep a special context for static initializer methods." )
                +padOpt( "kobjsens-types-for-context (false)", "Use types instead of allocation sites for context for context depth > 1." )
                +padOpt( "kobjsens-extra-array-context (false)", "For array allocations add one extra depth for context string." )
                +padOpt( "kobjsens-no-context-list ()", "Do not keep method context for types in this list." )
                +padOpt( "kobjsens-limit-heap-context ()", "Limit heap context for objects of types passed." );
    
        if( phaseName.equals( "cg.paddle" ) )
            return "Phase "+phaseName+":\n"+
                "\nPaddle is a BDD-based interprocedural analysis framework. It \nincludes points-to analysis, call graph construction, and \nvarious client analyses."
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (false)", "" )
                +padOpt( "verbose (false)", "Print detailed information about the execution of Paddle" )
                +padOpt( "conf", "Select Paddle configuration" )
                +padVal( "ofcg (default)", "On-the fly call graph" )
                
                +padVal( "cha", "CHA only" )
                
                +padVal( "cha-aot", "CHA ahead-of-time callgraph" )
                
                +padVal( "ofcg-aot", "OFCG-AOT callgraph" )
                
                +padVal( "cha-context-aot", "CHA-Context-AOT callgraph" )
                
                +padVal( "ofcg-context-aot", "OFCG-Context-AOT callgraph" )
                
                +padVal( "cha-context", "CHA-Context callgraph" )
                
                +padVal( "ofcg-context", "OFCG-Context callgraph" )
                
                +padOpt( "bdd (false)", "Use BDD version of Paddle" )
                +padOpt( "order (32)", "" )
                +padOpt( "dynamic-order", "" )
                +padOpt( "profile (false)", "Profile BDDs using JeddProfiler" )
                +padOpt( "verbosegc (false)", "Print memory usage at each BDD garbage collection." )
                +padOpt( "q", "Select queue implementation" )
                +padVal( "auto (default)", "Select queue implementation based on bdd option" )
                
                +padVal( "trad", "Normal worklist queue implementation" )
                
                +padVal( "bdd", "BDD-based queue implementation" )
                
                +padVal( "debug", "Debugging queue implementation" )
                
                +padVal( "trace", "Tracing queue implementation" )
                
                +padVal( "numtrace", "Number-tracing queue implementation" )
                
                +padOpt( "backend", "Select BDD backend" )
                +padVal( "auto (default)", "Select backend based on bdd option" )
                
                +padVal( "buddy", "BuDDy backend" )
                
                +padVal( "cudd", "CUDD backend" )
                
                +padVal( "sable", "SableJBDD backend" )
                
                +padVal( "javabdd", "JavaBDD backend" )
                
                +padVal( "none", "No BDDs" )
                
                +padOpt( "bdd-nodes (0)", "Number of BDD nodes to allocate (0=unlimited)" )
                +padOpt( "ignore-types (false)", "Make Paddle completely ignore declared types of variables" )
                +padOpt( "pre-jimplify (false)", "Jimplify all methods before starting Paddle" )
                +padOpt( "context", "Select context-sensitivity level" )
                +padVal( "insens (default)", "Builds a context-insensitive call graph" )
                
                +padVal( "1cfa", "Builds a 1-CFA call graph" )
                
                +padVal( "kcfa", "Builds a k-CFA call graph" )
                
                +padVal( "objsens", "Builds an object-sensitive call graph" )
                
                +padVal( "kobjsens", "Builds a k-object-sensitive call graph" )
                
                +padVal( "uniqkobjsens", "Builds a unique-k-object-sensitive call graph" )
                
                +padVal( "threadkobjsens", "Experimental option for thread-entry-point sensitivity" )
                
                +padOpt( "k (2)", "" )
                +padOpt( "context-heap (false)", "Treat allocation sites context-sensitively" )
                +padOpt( "rta (false)", "Emulate Rapid Type Analysis" )
                +padOpt( "field-based (false)", "Use a field-based rather than field-sensitive representation" )
                +padOpt( "types-for-sites (false)", "Represent objects by their actual type rather than allocation site" )
                +padOpt( "merge-stringbuffer (true)", "Represent all StringBuffers as one object" )
                +padOpt( "string-constants (false)", "Propagate all string constants, not just class names" )
                +padOpt( "simulate-natives (true)", "Simulate effects of native methods in standard class library" )
                +padOpt( "global-nodes-in-natives (false)", "Use global node to model variables in simulations of native methods" )
                +padOpt( "simple-edges-bidirectional (false)", "Equality-based analysis between variable nodes" )
                +padOpt( "this-edges (false)", "Use pointer assignment edges to model this parameters" )
                +padOpt( "precise-newinstance (true)", "Make newInstance only allocate objects of dynamic classes" )
                +padOpt( "propagator", "Select propagation algorithm" )
                +padVal( "auto (default)", "Select propagation algorithm based on bdd option" )
                
                +padVal( "iter", "Simple iterative algorithm" )
                
                +padVal( "worklist", "Fast, worklist-based algorithm" )
                
                +padVal( "alias", "Alias-edge based algorithm" )
                
                +padVal( "bdd", "BDD-based propagator" )
                
                +padVal( "incbdd", "Incrementalized BDD-based propagator" )
                
                +padOpt( "set-impl", "Select points-to set implementation" )
                +padVal( "hash", "Use Java HashSet" )
                
                +padVal( "bit", "Bit vector" )
                
                +padVal( "hybrid", "Hybrid representation using bit vector for large sets" )
                
                +padVal( "array", "Sorted array representation" )
                
                +padVal( "heintze", "Heintze's shared bit-vector and overflow list representation" )
                
                +padVal( "double (default)", "Double set representation for incremental propagation" )
                
                +padOpt( "double-set-old", "Select implementation of points-to set for old part of double set" )
                +padVal( "hash", "Use Java HashSet" )
                
                +padVal( "bit", "Bit vector" )
                
                +padVal( "hybrid (default)", "Hybrid representation using bit vector for large sets" )
                
                +padVal( "array", "Sorted array representation" )
                
                +padVal( "heintze", "Heintze's shared bit-vector and overflow list representation" )
                
                +padOpt( "double-set-new", "Select implementation of points-to set for new part of double set" )
                +padVal( "hash", "Use Java HashSet" )
                
                +padVal( "bit", "Bit vector" )
                
                +padVal( "hybrid (default)", "Hybrid representation using bit vector for large sets" )
                
                +padVal( "array", "Sorted array representation" )
                
                +padVal( "heintze", "Heintze's shared bit-vector and overflow list representation" )
                
                +padOpt( "context-counts (false)", "Print number of contexts for each method" )
                +padOpt( "total-context-counts (false)", "Print total number of contexts" )
                +padOpt( "method-context-counts (false)", "Print number of contexts for each method" )
                +padOpt( "set-mass (false)", "Calculate statistics about points-to set sizes" )
                +padOpt( "number-nodes (true)", "Print node numbers in dumps" );
    
        if( phaseName.equals( "wstp" ) )
            return "Phase "+phaseName+":\n"+
                "\nSoot can perform whole-program analyses. In \nwhole-shimple mode, Soot applies the contents of the \nWhole-Shimple Transformation Pack to the scene as a \nwhole after constructing a call graph for the program. \nIn an unmodified copy of Soot the Whole-Shimple Transformation \nPack is empty."
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" );
    
        if( phaseName.equals( "wsop" ) )
            return "Phase "+phaseName+":\n"+
                "\nIf Soot is running in whole shimple mode and the \nWhole-Shimple Optimization Pack is enabled, the \npack's transformations are applied to the scene as a \nwhole after construction of the call graph and \napplication of any enabled Whole-Shimple \nTransformations. In an unmodified copy of Soot the \nWhole-Shimple Optimization Pack is empty."
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (false)", "" );
    
        if( phaseName.equals( "wjtp" ) )
            return "Phase "+phaseName+":\n"+
                "\nSoot can perform whole-program analyses. In \nwhole-program mode, Soot applies the contents of the \nWhole-Jimple Transformation Pack to the scene as a \nwhole after constructing a call graph for the program."
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" );
    
        if( phaseName.equals( "wjtp.mhp" ) )
            return "Phase "+phaseName+":\n"+
                "\nMay Happen in Parallel (MHP) Analyses determine \nwhat program statements may be run by different \nthreads concurrently. This phase does not perform any \ntransformation. "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (false)", "" );
    
        if( phaseName.equals( "wjtp.tn" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Lock Allocator finds critical sections \n(synchronized regions) in Java programs and assigns \nlocks for execution on both optimistic and \npessimistic JVMs. It can also be used to analyze the existing \nlocks. "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (false)", "" )
                +padOpt( "locking-scheme", "Selects the granularity of the generated lock allocation" )
                +padVal( "medium-grained (default)", "Use a runtime object for synchronization where possible" )
                
                +padVal( "coarse-grained", "Use static objects for synchronization" )
                
                +padVal( "single-static", "Use just one static synchronization object for all transactional regions" )
                
                +padVal( "leave-original", "Analyse the existing lock structure without making changes" )
                
                +padOpt( "avoid-deadlock (true)", "Perform Deadlock Avoidance" )
                +padOpt( "open-nesting (true)", "Use an open nesting model" )
                +padOpt( "do-mhp (true)", "Perform a May-Happen-in-Parallel analysis" )
                +padOpt( "do-tlo (true)", "Perform a Local-Objects analysis" )
                +padOpt( "print-graph (false)", "Print topological graph of transactions" )
                +padOpt( "print-table (false)", "Print table of transactions" )
                +padOpt( "print-debug (false)", "Print debugging info" );
    
        if( phaseName.equals( "wjtp.rdc" ) )
            return "Phase "+phaseName+":\n"+
                "\nRename duplicated classes when the file system is not case \nsensitive. If the file system is case sensitive, this phase does \nnothing. "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (false)", "" )
                +padOpt( "fcn", "Set  for the fixed class names." );
    
        if( phaseName.equals( "wjop" ) )
            return "Phase "+phaseName+":\n"+
                "\nIf Soot is running in whole program mode and the \nWhole-Jimple Optimization Pack is enabled, the \npack's transformations are applied to the scene as a \nwhole after construction of the call graph and \napplication of any enabled Whole-Jimple \nTransformations."
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (false)", "" );
    
        if( phaseName.equals( "wjop.smb" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Static Method Binder statically binds \nmonomorphic call sites. That is, it searches the \ncall graph for virtual method invocations that can \nbe determined statically to call only a single \nimplementation of the called method. Then it replaces such \nvirtual invocations with invocations of a static \ncopy of the single called implementation. "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (false)", "" )
                +padOpt( "insert-null-checks (true)", "" )
                +padOpt( "insert-redundant-casts (true)", "" )
                +padOpt( "allowed-modifier-changes", "" )
                +padVal( "unsafe (default)", "" )
                
                +padVal( "safe", "" )
                
                +padVal( "none", "" )
                ;
    
        if( phaseName.equals( "wjop.si" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Static Inliner visits all call sites in the \ncall graph in a bottom-up fashion, replacing \nmonomorphic calls with inlined copies of the invoked \nmethods. "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" )
                +padOpt( "rerun-jb (true)", "" )
                +padOpt( "insert-null-checks (true)", "" )
                +padOpt( "insert-redundant-casts (true)", "" )
                +padOpt( "allowed-modifier-changes", "" )
                +padVal( "unsafe (default)", "" )
                
                +padVal( "safe", "" )
                
                +padVal( "none", "" )
                
                +padOpt( "expansion-factor (3)", "" )
                +padOpt( "max-container-size (5000)", "" )
                +padOpt( "max-inlinee-size (20)", "" );
    
        if( phaseName.equals( "wjap" ) )
            return "Phase "+phaseName+":\n"+
                "\nSome analyses do not transform Jimple body \ndirectly, but annotate statements or values with \ntags. Whole-Jimple annotation pack provides a place \nfor annotation-oriented analyses in whole program mode."
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" );
    
        if( phaseName.equals( "wjap.ra" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Rectangular Array Finder traverses Jimple \nstatements based on the static call graph, and \nfinds array variables which always hold rectangular \ntwo-dimensional array objects. In Java, a \nmulti-dimensional array is an array of arrays, which \nmeans the shape of the array can be ragged. Nevertheless, many \napplications use rectangular arrays. Knowing that \nan array is rectangular can be very helpful in \nproving safe array bounds checks. The \nRectangular Array Finder does not change the \nprogram being analyzed. Its results are used by the Array Bound \nChecker."
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (false)", "" );
    
        if( phaseName.equals( "wjap.umt" ) )
            return "Phase "+phaseName+":\n"+
                "\nUses the call graph to determine which methods are unreachable \nand adds color tags so they can be highlighted in a source \nbrowser."
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (false)", "" );
    
        if( phaseName.equals( "wjap.uft" ) )
            return "Phase "+phaseName+":\n"+
                "\nUses the call graph to determine which fields are unreachable \nand adds color tags so they can be highlighted in a source \nbrowser."
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (false)", "" );
    
        if( phaseName.equals( "wjap.tqt" ) )
            return "Phase "+phaseName+":\n"+
                "\nDetermines which methods and fields have qualifiers that could \nbe tightened. For example: if a field or method has the \nqualifier of public but is only used within the declaring class \nit could be private. This, this field or method is tagged with \ncolor tags so that the results can be highlighted in a source \nbrowser."
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (false)", "" );
    
        if( phaseName.equals( "wjap.cgg" ) )
            return "Phase "+phaseName+":\n"+
                "\nCreates graphical call graph."
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (false)", "" )
                +padOpt( "show-lib-meths (false)", "" );
    
        if( phaseName.equals( "wjap.purity" ) )
            return "Phase "+phaseName+":\n"+
                "\nPurity anaysis implemented by Antoine Mine and \nbased on the paper A Combined Pointer and Purity \nAnalysis for Java Programs by Alexandru Salcianu \nand Martin Rinard. "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (false)", "" )
                +padOpt( "dump-summaries (true)", "" )
                +padOpt( "dump-cg (false)", "" )
                +padOpt( "dump-intra (false)", "" )
                +padOpt( "print (true)", "" )
                +padOpt( "annotate (true)", "Marks pure methods with a purity bytecode attribute" )
                +padOpt( "verbose (false)", "" );
    
        if( phaseName.equals( "shimple" ) )
            return "Phase "+phaseName+":\n"+
                "\nShimple Control sets parameters which apply \nthroughout the creation and manipulation of Shimple \nbodies. Shimple is Soot's SSA representation."
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" )
                +padOpt( "node-elim-opt (true)", "Node elimination optimizations" )
                +padOpt( "standard-local-names (false)", "Uses naming scheme of the Local Name           Standardizer." )
                +padOpt( "extended (false)", "Compute extended SSA (SSI) form." )
                +padOpt( "debug (false)", "Enables debugging output, if any." );
    
        if( phaseName.equals( "stp" ) )
            return "Phase "+phaseName+":\n"+
                "\nWhen the Shimple representation is produced, Soot \napplies the contents of the Shimple Transformation \nPack to each method under analysis. This pack \ncontains no transformations in an unmodified version \nof Soot. "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" );
    
        if( phaseName.equals( "sop" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Shimple Optimization Pack contains \ntransformations that perform optimizations on \nShimple, Soot's SSA representation. "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (false)", "" );
    
        if( phaseName.equals( "sop.cpf" ) )
            return "Phase "+phaseName+":\n"+
                "\nA powerful constant propagator and folder based \non an algorithm sketched by Cytron et al that \ntakes conditional control flow into account. This \noptimization demonstrates some of the benefits of \nSSA -- particularly the fact that Phi nodes \nrepresent natural merge points in the control \nflow. "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" )
                +padOpt( "prune-cfg (true)", "Take advantage of CFG optimization             opportunities." );
    
        if( phaseName.equals( "jtp" ) )
            return "Phase "+phaseName+":\n"+
                "\nSoot applies the contents of the Jimple \nTransformation Pack to each method under analysis. \nThis pack contains no transformations in an unmodified \nversion of Soot. "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" );
    
        if( phaseName.equals( "jop" ) )
            return "Phase "+phaseName+":\n"+
                "\nWhen Soot's Optimize option is on, Soot applies the \nJimple Optimization Pack to every JimpleBody in \napplication classes. This section lists the default \ntransformations in the Jimple Optimization Pack. "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (false)", "Eliminates common subexpressions" );
    
        if( phaseName.equals( "jop.cse" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Common Subexpression Eliminator runs an \navailable expressions analysis on the method body, \nthen eliminates common subexpressions. \nThis implementation is especially slow, as it \nruns on individual statements rather than on basic \nblocks. A better implementation (which would find \nmost common subexpressions, but not all) would use \nbasic blocks instead. This \nimplementation is also slow because the flow universe is \nexplicitly created; it need not be. A better \nimplementation would implicitly compute the kill \nsets at every node. Because of its \ncurrent slowness, this transformation is not \nenabled by default. "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (false)", "" )
                +padOpt( "naive-side-effect (false)", "Use naive side effect analysis even if interprocedural information is available" );
    
        if( phaseName.equals( "jop.bcm" ) )
            return "Phase "+phaseName+":\n"+
                "\nBusy Code Motion is a straightforward \nimplementation of Partial Redundancy Elimination. \nThis implementation is not very aggressive. Lazy \nCode Motion is an improved version which should be \nused instead of Busy Code Motion. "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (false)", "" )
                +padOpt( "naive-side-effect (false)", "Use a naive side effect analysis even if interprocedural information is available" );
    
        if( phaseName.equals( "jop.lcm" ) )
            return "Phase "+phaseName+":\n"+
                "\nLazy Code Motion is an enhanced version of Busy \nCode Motion, a Partial Redundancy Eliminator. Before \ndoing Partial Redundancy Elimination, this \noptimization performs loop inversion (turning while loops \ninto do while loops inside an if statement). \nThis allows the Partial Redundancy Eliminator to \noptimize loop invariants of while loops. "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (false)", "" )
                +padOpt( "safety", "" )
                +padVal( "safe (default)", "" )
                
                +padVal( "medium", "" )
                
                +padVal( "unsafe", "" )
                
                +padOpt( "unroll (true)", "" )
                +padOpt( "naive-side-effect (false)", "Use a naive side effect analysis even if interprocedural information is available" );
    
        if( phaseName.equals( "jop.cp" ) )
            return "Phase "+phaseName+":\n"+
                "\nThis phase performs cascaded copy propagation."
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" )
                +padOpt( "only-regular-locals (false)", "" )
                +padOpt( "only-stack-locals (false)", "" );
    
        if( phaseName.equals( "jop.cpf" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Jimple Constant Propagator and Folder evaluates \nany expressions consisting entirely of compile-time \nconstants, for example 2 * 3, and replaces the \nexpression with the constant result, in this case 6. \n"
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" );
    
        if( phaseName.equals( "jop.cbf" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Conditional Branch Folder statically evaluates \nthe conditional expression of Jimple if statements. \nIf the condition is identically true or \nfalse, the Folder replaces the conditional branch \nstatement with an unconditional goto statement. "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" );
    
        if( phaseName.equals( "jop.dae" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Dead Assignment Eliminator eliminates \nassignment statements to locals whose values are not \nsubsequently used, unless evaluating the right-hand \nside of the assignment may cause side-effects. \n"
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" )
                +padOpt( "only-tag (false)", "" )
                +padOpt( "only-stack-locals (false)", "" );
    
        if( phaseName.equals( "jop.nce" ) )
            return "Phase "+phaseName+":\n"+
                "\nReplaces statements 'if(x!=null) goto y' with 'goto \ny' if x is known to be non-null or with 'nop' if it \nis known to be null, etc. Generates dead code and is \nhence followed by unreachable code elimination. \nDisabled by default because it can be expensive on \nmethods with many locals. "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (false)", "" );
    
        if( phaseName.equals( "jop.uce1" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Unreachable Code Eliminator removes unreachable \ncode and traps whose catch blocks are empty. \n"
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" )
                +padOpt( "remove-unreachable-traps (false)", "" );
    
        if( phaseName.equals( "jop.ubf1" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Unconditional Branch Folder removes \nunnecessary `goto' statements from a JimpleBody. \nIf a goto statement's target is the next instruction, \nthen the statement is removed. If a goto's target \nis another goto, with target y, then the first \nstatement's target is changed to y. If \nsome if statement's target is a goto statement, \nthen the if's target can be replaced with the goto's \ntarget. (These situations can result from other \noptimizations, and branch folding may itself \ngenerate more unreachable code.)"
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" );
    
        if( phaseName.equals( "jop.uce2" ) )
            return "Phase "+phaseName+":\n"+
                "\nAnother iteration of the Unreachable Code \nEliminator. "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" )
                +padOpt( "remove-unreachable-traps (false)", "" );
    
        if( phaseName.equals( "jop.ubf2" ) )
            return "Phase "+phaseName+":\n"+
                "\nAnother iteration of the Unconditional Branch \nFolder. "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" );
    
        if( phaseName.equals( "jop.ule" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Unused Local Eliminator phase removes any \nunused locals from the method. "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" );
    
        if( phaseName.equals( "jap" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Jimple Annotation Pack contains phases which add \nannotations to Jimple bodies individually (as opposed \nto the Whole-Jimple Annotation Pack, which adds \nannotations based on the analysis of the whole \nprogram). "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" );
    
        if( phaseName.equals( "jap.npc" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Null Pointer Checker finds instruction which \nhave the potential to throw NullPointerExceptions \nand adds annotations indicating whether or not the \npointer being dereferenced can be determined \nstatically not to be null. "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (false)", "" )
                +padOpt( "only-array-ref (false)", "Annotate only array references" )
                +padOpt( "profiling (false)", "Insert instructions to count safe pointer accesses" );
    
        if( phaseName.equals( "jap.npcolorer" ) )
            return "Phase "+phaseName+":\n"+
                "\nProduce colour tags that the Soot plug-in for \nEclipse can use to highlight null and non-null \nreferences. "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (false)", "" );
    
        if( phaseName.equals( "jap.abc" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Array Bound Checker performs a static \nanalysis to determine which array bounds checks \nmay safely be eliminated and then annotates \nstatements with the results of the analysis. If \nSoot is in whole-program mode, the Array Bound Checker can \nuse the results provided by the Rectangular Array Finder."
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (false)", "" )
                +padOpt( "with-all (false)", "" )
                +padOpt( "with-cse (false)", "" )
                +padOpt( "with-arrayref (false)", "" )
                +padOpt( "with-fieldref (false)", "" )
                +padOpt( "with-classfield (false)", "" )
                +padOpt( "with-rectarray (false)", "" )
                +padOpt( "profiling (false)", "Profile the results of array bounds check analysis." )
                +padOpt( "add-color-tags (false)", "Add color tags to results of array bound check analysis." );
    
        if( phaseName.equals( "jap.profiling" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Profiling Generator inserts the method \ninvocations required to initialize and to report \nthe results of any profiling performed by the Null \nPointer Checker and Array Bound Checker. Users of \nthe Profiling Generator must provide a \nMultiCounter class implementing the methods invoked. For \ndetails, see the ProfilingGenerator source code. \n"
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (false)", "" )
                +padOpt( "notmainentry (false)", "Instrument runBenchmark() instead of main()" );
    
        if( phaseName.equals( "jap.sea" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Side Effect Tagger uses the \nactive invoke graph to produce side-effect attributes, as \ndescribed in the Spark thesis, chapter 6."
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (false)", "" )
                +padOpt( "naive (false)", "" );
    
        if( phaseName.equals( "jap.fieldrw" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Field Read/Write Tagger uses the active \ninvoke graph to produce tags indicating which \nfields may be read or written by each statement, \nincluding invoke statements."
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (false)", "" )
                +padOpt( "threshold (100)", "" );
    
        if( phaseName.equals( "jap.cgtagger" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Call Graph Tagger produces LinkTags based on \nthe call graph. The Eclipse plugin uses these tags \nto produce linked popup lists which indicate the \nsource and target methods of the statement. \nSelecting a link from the list moves the cursor to \nthe indicated method. "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (false)", "" );
    
        if( phaseName.equals( "jap.parity" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Parity Tagger produces StringTags and ColorTags \nindicating the parity of a variable (even, odd, top, \nor bottom). The eclipse plugin can use tooltips and \nvariable colouring to display the information in \nthese tags. For example, even variables (such as x \nin x = 2) are coloured yellow. "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (false)", "" );
    
        if( phaseName.equals( "jap.pat" ) )
            return "Phase "+phaseName+":\n"+
                "\nFor each method with parameters of reference type, this tagger \nindicates the aliasing relationships between the parameters \nusing colour tags. Parameters that may be aliased are the same \ncolour. Parameters that may not be aliased are in different \ncolours."
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (false)", "" );
    
        if( phaseName.equals( "jap.lvtagger" ) )
            return "Phase "+phaseName+":\n"+
                "\nColors live variables."
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (false)", "" );
    
        if( phaseName.equals( "jap.rdtagger" ) )
            return "Phase "+phaseName+":\n"+
                "\nFor each use of a local in a stmt creates a link to the reaching \ndef."
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (false)", "" );
    
        if( phaseName.equals( "jap.che" ) )
            return "Phase "+phaseName+":\n"+
                "\nIndicates whether cast checks can be eliminated."
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (false)", "" );
    
        if( phaseName.equals( "jap.umt" ) )
            return "Phase "+phaseName+":\n"+
                "\nWhen the whole-program analysis determines a method to be \nunreachable, this transformer inserts an assertion into the \nmethod to check that it is indeed unreachable."
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (false)", "" );
    
        if( phaseName.equals( "jap.lit" ) )
            return "Phase "+phaseName+":\n"+
                "\nAn expression whose operands are constant or have reaching \ndefinitions from outside the loop body are tagged as loop \ninvariant."
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (false)", "" );
    
        if( phaseName.equals( "jap.aet" ) )
            return "Phase "+phaseName+":\n"+
                "\nA each statement a set of available expressions is after the \nstatement is added as a tag."
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (false)", "" )
                +padOpt( "kind", "" )
                +padVal( "optimistic (default)", "" )
                
                +padVal( "pessimistic", "" )
                ;
    
        if( phaseName.equals( "jap.dmt" ) )
            return "Phase "+phaseName+":\n"+
                "\nProvides link tags at a statement to all of the satements \ndominators."
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (false)", "" );
    
        if( phaseName.equals( "gb" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Grimp Body Creation phase creates a GrimpBody for \neach source method. It is run only if the output \nformat is grimp or grimple, or if class files are \nbeing output and the Via Grimp option has been \nspecified. "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" );
    
        if( phaseName.equals( "gb.a1" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Grimp Pre-folding Aggregator combines some \nlocal variables, finding definitions with only a \nsingle use and removing the definition after \nreplacing the use with the definition's right-hand \nside, if it is safe to do so. While the mechanism is \nthe same as that employed by the Jimple Local Aggregator, there \nis more scope for aggregation because of Grimp's more \ncomplicated expressions. "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" )
                +padOpt( "only-stack-locals (true)", "" );
    
        if( phaseName.equals( "gb.cf" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Grimp Constructor Folder combines new \nstatements with the specialinvoke statement that \ncalls the new object's constructor. For example, it \nturns r2 = new java.util.ArrayList; \nr2.init(); into \nr2 = new java.util.ArrayList(); "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" );
    
        if( phaseName.equals( "gb.a2" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Grimp Post-folding Aggregator combines local \nvariables after constructors have been folded. \nConstructor folding typically introduces new \nopportunities for aggregation, since when a sequence \nof instructions like r2 = new \njava.util.ArrayList; r2.init(); r3 = \nr2 is replaced by \nr2 = new java.util.ArrayList(); r3 = r2 \nthe invocation of init no longer represents a potential \nside-effect separating the two definitions, so \nthey can be combined into r3 \n= new java.util.ArrayList(); (assuming there \nare no subsequent uses of r2). "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" )
                +padOpt( "only-stack-locals (true)", "" );
    
        if( phaseName.equals( "gb.ule" ) )
            return "Phase "+phaseName+":\n"+
                "\nThis phase removes any locals that are unused after \nconstructor folding and aggregation. "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" );
    
        if( phaseName.equals( "gop" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Grimp Optimization pack performs optimizations on \nGrimpBodys (currently there are no optimizations \nperformed specifically on GrimpBodys, and the pack is \nempty). It is run only if the output format is grimp or \ngrimple, or if class files are being output and the Via \nGrimp option has been specified. "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (false)", "" );
    
        if( phaseName.equals( "bb" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Baf Body Creation phase creates a \nBafBody from each source method. It is run if the \noutput format is baf or b, or if class files are being \noutput and the Via Grimp option has not been \nspecified. "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" );
    
        if( phaseName.equals( "bb.lso" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Load Store Optimizer replaces some combinations \nof loads to and stores from local variables with stack \ninstructions. A simple example would be the replacement of \nstore.r $r2; load.r $r2; \nwith dup1.r \nin cases where the value of r2 is not used \nsubsequently. "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" )
                +padOpt( "debug (false)", "" )
                +padOpt( "inter (false)", "" )
                +padOpt( "sl (true)", "" )
                +padOpt( "sl2 (false)", "" )
                +padOpt( "sll (true)", "" )
                +padOpt( "sll2 (false)", "" );
    
        if( phaseName.equals( "bb.pho" ) )
            return "Phase "+phaseName+":\n"+
                "\nApplies peephole optimizations to the Baf \nintermediate representation. Individual \noptimizations must be implemented by classes \nimplementing the Peephole interface. The Peephole \nOptimizer reads the names of the Peephole classes at \nruntime from the file peephole.dat and loads them \ndynamically. Then it continues to apply the \nPeepholes repeatedly until none of them are able to \nperform any further optimizations. \nSoot provides only one Peephole, named \nExamplePeephole, which is not enabled by the delivered \npeephole.dat file. ExamplePeephole removes all \ncheckcast instructions."
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" );
    
        if( phaseName.equals( "bb.ule" ) )
            return "Phase "+phaseName+":\n"+
                "\nThis phase removes any locals that are unused after \nload store optimization and peephole optimization. \n"
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" );
    
        if( phaseName.equals( "bb.lp" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Local Packer attempts to minimize the number of \nlocal variables required in a method by reusing the \nsame variable for disjoint DU-UD webs. Conceptually, \nit is the inverse of the Local Splitter. "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" )
                +padOpt( "unsplit-original-locals (false)", "" );
    
        if( phaseName.equals( "bop" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Baf Optimization pack performs optimizations on \nBafBodys (currently there are no optimizations performed \nspecifically on BafBodys, and the pack is empty). It \nis run only if the output format is baf or b, or \nif class files are being output and the Via Grimp option \nhas not been specified. "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (false)", "" );
    
        if( phaseName.equals( "tag" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Tag Aggregator pack aggregates tags attached to \nindividual units into a code attribute for each \nmethod, so that these attributes can be encoded in \nJava class files."
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" );
    
        if( phaseName.equals( "tag.ln" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Line Number Tag Aggregator aggregates line \nnumber tags."
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" );
    
        if( phaseName.equals( "tag.an" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Array Bounds and Null Pointer Tag Aggregator \naggregates tags produced by the Array Bound Checker \nand Null Pointer Checker."
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (false)", "" );
    
        if( phaseName.equals( "tag.dep" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Dependence Tag Aggregator aggregates \ntags produced by the Side Effect Tagger."
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (false)", "" );
    
        if( phaseName.equals( "tag.fieldrw" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe Field Read/Write Tag Aggregator aggregates \nfield read/write tags produced by the Field \nRead/Write Tagger, phase jap.fieldrw. "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (false)", "" );
    
        if( phaseName.equals( "db" ) )
            return "Phase "+phaseName+":\n"+
                "\nThe decompile (Dava) option is set using the -f dava \noptions in Soot. Options provided by Dava are added to \nthis dummy phase so as not to clutter the soot general \narguments. -p db (option name):(value) will be used to \nset all required values for Dava. "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" )
                +padOpt( "source-is-javac (true)", "" );
    
        if( phaseName.equals( "db.transformations" ) )
            return "Phase "+phaseName+":\n"+
                "\n	 The transformations implemented using AST Traversal and \nstructural flow analses on Dava's AST 	 "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" );
    
        if( phaseName.equals( "db.renamer" ) )
            return "Phase "+phaseName+":\n"+
                "\nIf set, the renaming analyses implemented in Dava are applied to \neach method body being decompiled. The analyses use heuristics \nto choose potentially better names for local variables. (As of \nFebruary 14th 2006, work is still under progress on these \nanalyses (dava.toolkits.base.renamer). 	 "
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (false)", "" );
    
        if( phaseName.equals( "db.deobfuscate" ) )
            return "Phase "+phaseName+":\n"+
                "\nCertain analyses make sense only when the bytecode is obfuscated \ncode. There are plans to implement such analyses and \napply them on methods only if this flag is set. Dead \nCode elimination which includes removing code guarded by some \ncondition which is always false or always true is one such \nanalysis. Another suggested analysis is giving default names \nto classes and fields. Onfuscators love to use weird names \nfor fields and classes and even a simple re-naming of these \ncould be a good help to the user. Another more \nadvanced analysis would be to check for redundant constant \nfields added by obfuscators and then remove uses of \nthese constant fields from the code."
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" );
    
        if( phaseName.equals( "db.force-recompile" ) )
            return "Phase "+phaseName+":\n"+
                "\nWhile decompiling we have to be clear what our aim is: do we \nwant to convert bytecode to Java syntax and stay as \nclose to the actual execution of bytecode or do we want \nrecompilably Java source representing the bytecode. \nThis distinction is important because some restrictions present \nin Java source are absent from the bytecode. Examples \nof this include that fact that in Java a call to a constructor \nor super needs to be the first statement in a \nconstructors body. This restriction is absent from the bytecode. \nSimilarly final fields HAVE to be initialized once and \nonly once in either the static initializer (static fields) or \nall the constructors (non-static fields). Additionally \nthe fields should be initialized on all possible execution \npaths. These restrictions are again absent from the bytecode. \nIn doing a one-one conversion of bytecode to Java source \nthen no attempt should be made to fix any of these and similar \nproblems in the Java source. However, if the aim is to \nget recompilable code then these and similar issues need to be \nfixed. Setting the force-recompilability flag will \nensure that the decompiler tries its best to produce \nrecompilable Java source."
                +"\n\nRecognized options (with default values):\n"
                +padOpt( "enabled (true)", "" );
    

        return "Unrecognized phase: "+phaseName;
    }
  
    public static String getDeclaredOptionsForPhase( String phaseName ) {
    
        if( phaseName.equals( "jb" ) )
            return ""
                +"enabled "
                +"use-original-names "
                +"preserve-source-annotations ";
    
        if( phaseName.equals( "jb.ls" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "jb.a" ) )
            return ""
                +"enabled "
                +"only-stack-locals ";
    
        if( phaseName.equals( "jb.ule" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "jb.tr" ) )
            return ""
                +"enabled "
                +"ignore-wrong-staticness "
                +"use-older-type-assigner "
                +"compare-type-assigners ";
    
        if( phaseName.equals( "jb.ulp" ) )
            return ""
                +"enabled "
                +"unsplit-original-locals ";
    
        if( phaseName.equals( "jb.lns" ) )
            return ""
                +"enabled "
                +"only-stack-locals ";
    
        if( phaseName.equals( "jb.cp" ) )
            return ""
                +"enabled "
                +"only-regular-locals "
                +"only-stack-locals ";
    
        if( phaseName.equals( "jb.dae" ) )
            return ""
                +"enabled "
                +"only-stack-locals ";
    
        if( phaseName.equals( "jb.cp-ule" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "jb.lp" ) )
            return ""
                +"enabled "
                +"unsplit-original-locals ";
    
        if( phaseName.equals( "jb.ne" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "jb.uce" ) )
            return ""
                +"enabled "
                +"remove-unreachable-traps ";
    
        if( phaseName.equals( "jb.tt" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "jj" ) )
            return ""
                +"enabled "
                +"use-original-names ";
    
        if( phaseName.equals( "jj.ls" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "jj.a" ) )
            return ""
                +"enabled "
                +"only-stack-locals ";
    
        if( phaseName.equals( "jj.ule" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "jj.tr" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "jj.ulp" ) )
            return ""
                +"enabled "
                +"unsplit-original-locals ";
    
        if( phaseName.equals( "jj.lns" ) )
            return ""
                +"enabled "
                +"only-stack-locals ";
    
        if( phaseName.equals( "jj.cp" ) )
            return ""
                +"enabled "
                +"only-regular-locals "
                +"only-stack-locals ";
    
        if( phaseName.equals( "jj.dae" ) )
            return ""
                +"enabled "
                +"only-stack-locals ";
    
        if( phaseName.equals( "jj.cp-ule" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "jj.lp" ) )
            return ""
                +"enabled "
                +"unsplit-original-locals ";
    
        if( phaseName.equals( "jj.ne" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "jj.uce" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "wjpp" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "wspp" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "cg" ) )
            return ""
                +"enabled "
                +"safe-forname "
                +"safe-newinstance "
                +"verbose "
                +"jdkver "
                +"all-reachable "
                +"implicit-entry "
                +"trim-clinit "
                +"reflection-log "
                +"guards ";
    
        if( phaseName.equals( "cg.cha" ) )
            return ""
                +"enabled "
                +"verbose ";
    
        if( phaseName.equals( "cg.spark" ) )
            return ""
                +"enabled "
                +"verbose "
                +"ignore-types "
                +"force-gc "
                +"pre-jimplify "
                +"vta "
                +"rta "
                +"field-based "
                +"types-for-sites "
                +"merge-stringbuffer "
                +"string-constants "
                +"simulate-natives "
                +"empties-as-allocs "
                +"simple-edges-bidirectional "
                +"on-fly-cg "
                +"simplify-offline "
                +"simplify-sccs "
                +"ignore-types-for-sccs "
                +"propagator "
                +"set-impl "
                +"double-set-old "
                +"double-set-new "
                +"dump-html "
                +"dump-pag "
                +"dump-solution "
                +"topo-sort "
                +"dump-types "
                +"class-method-var "
                +"dump-answer "
                +"add-tags "
                +"set-mass "
                +"cs-demand "
                +"lazy-pts "
                +"traversal "
                +"passes "
                +"geom-pta "
                +"geom-encoding "
                +"geom-worklist "
                +"geom-dump-verbose "
                +"geom-verify-name "
                +"geom-eval "
                +"geom-trans "
                +"geom-frac-base "
                +"geom-blocking "
                +"geom-runs "
                +"kobjsens "
                +"kobjsens-context-for-static-inits "
                +"kobjsens-types-for-context "
                +"kobjsens-extra-array-context "
                +"kobjsens-no-context-list "
                +"kobjsens-limit-heap-context ";
    
        if( phaseName.equals( "cg.paddle" ) )
            return ""
                +"enabled "
                +"verbose "
                +"conf "
                +"bdd "
                +"order "
                +"dynamic-order "
                +"profile "
                +"verbosegc "
                +"q "
                +"backend "
                +"bdd-nodes "
                +"ignore-types "
                +"pre-jimplify "
                +"context "
                +"k "
                +"context-heap "
                +"rta "
                +"field-based "
                +"types-for-sites "
                +"merge-stringbuffer "
                +"string-constants "
                +"simulate-natives "
                +"global-nodes-in-natives "
                +"simple-edges-bidirectional "
                +"this-edges "
                +"precise-newinstance "
                +"propagator "
                +"set-impl "
                +"double-set-old "
                +"double-set-new "
                +"context-counts "
                +"total-context-counts "
                +"method-context-counts "
                +"set-mass "
                +"number-nodes ";
    
        if( phaseName.equals( "wstp" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "wsop" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "wjtp" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "wjtp.mhp" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "wjtp.tn" ) )
            return ""
                +"enabled "
                +"locking-scheme "
                +"avoid-deadlock "
                +"open-nesting "
                +"do-mhp "
                +"do-tlo "
                +"print-graph "
                +"print-table "
                +"print-debug ";
    
        if( phaseName.equals( "wjtp.rdc" ) )
            return ""
                +"enabled "
                +"fcn ";
    
        if( phaseName.equals( "wjop" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "wjop.smb" ) )
            return ""
                +"enabled "
                +"insert-null-checks "
                +"insert-redundant-casts "
                +"allowed-modifier-changes ";
    
        if( phaseName.equals( "wjop.si" ) )
            return ""
                +"enabled "
                +"rerun-jb "
                +"insert-null-checks "
                +"insert-redundant-casts "
                +"allowed-modifier-changes "
                +"expansion-factor "
                +"max-container-size "
                +"max-inlinee-size ";
    
        if( phaseName.equals( "wjap" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "wjap.ra" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "wjap.umt" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "wjap.uft" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "wjap.tqt" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "wjap.cgg" ) )
            return ""
                +"enabled "
                +"show-lib-meths ";
    
        if( phaseName.equals( "wjap.purity" ) )
            return ""
                +"enabled "
                +"dump-summaries "
                +"dump-cg "
                +"dump-intra "
                +"print "
                +"annotate "
                +"verbose ";
    
        if( phaseName.equals( "shimple" ) )
            return ""
                +"enabled "
                +"node-elim-opt "
                +"standard-local-names "
                +"extended "
                +"debug ";
    
        if( phaseName.equals( "stp" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "sop" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "sop.cpf" ) )
            return ""
                +"enabled "
                +"prune-cfg ";
    
        if( phaseName.equals( "jtp" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "jop" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "jop.cse" ) )
            return ""
                +"enabled "
                +"naive-side-effect ";
    
        if( phaseName.equals( "jop.bcm" ) )
            return ""
                +"enabled "
                +"naive-side-effect ";
    
        if( phaseName.equals( "jop.lcm" ) )
            return ""
                +"enabled "
                +"safety "
                +"unroll "
                +"naive-side-effect ";
    
        if( phaseName.equals( "jop.cp" ) )
            return ""
                +"enabled "
                +"only-regular-locals "
                +"only-stack-locals ";
    
        if( phaseName.equals( "jop.cpf" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "jop.cbf" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "jop.dae" ) )
            return ""
                +"enabled "
                +"only-tag "
                +"only-stack-locals ";
    
        if( phaseName.equals( "jop.nce" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "jop.uce1" ) )
            return ""
                +"enabled "
                +"remove-unreachable-traps ";
    
        if( phaseName.equals( "jop.ubf1" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "jop.uce2" ) )
            return ""
                +"enabled "
                +"remove-unreachable-traps ";
    
        if( phaseName.equals( "jop.ubf2" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "jop.ule" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "jap" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "jap.npc" ) )
            return ""
                +"enabled "
                +"only-array-ref "
                +"profiling ";
    
        if( phaseName.equals( "jap.npcolorer" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "jap.abc" ) )
            return ""
                +"enabled "
                +"with-all "
                +"with-cse "
                +"with-arrayref "
                +"with-fieldref "
                +"with-classfield "
                +"with-rectarray "
                +"profiling "
                +"add-color-tags ";
    
        if( phaseName.equals( "jap.profiling" ) )
            return ""
                +"enabled "
                +"notmainentry ";
    
        if( phaseName.equals( "jap.sea" ) )
            return ""
                +"enabled "
                +"naive ";
    
        if( phaseName.equals( "jap.fieldrw" ) )
            return ""
                +"enabled "
                +"threshold ";
    
        if( phaseName.equals( "jap.cgtagger" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "jap.parity" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "jap.pat" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "jap.lvtagger" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "jap.rdtagger" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "jap.che" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "jap.umt" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "jap.lit" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "jap.aet" ) )
            return ""
                +"enabled "
                +"kind ";
    
        if( phaseName.equals( "jap.dmt" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "gb" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "gb.a1" ) )
            return ""
                +"enabled "
                +"only-stack-locals ";
    
        if( phaseName.equals( "gb.cf" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "gb.a2" ) )
            return ""
                +"enabled "
                +"only-stack-locals ";
    
        if( phaseName.equals( "gb.ule" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "gop" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "bb" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "bb.lso" ) )
            return ""
                +"enabled "
                +"debug "
                +"inter "
                +"sl "
                +"sl2 "
                +"sll "
                +"sll2 ";
    
        if( phaseName.equals( "bb.pho" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "bb.ule" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "bb.lp" ) )
            return ""
                +"enabled "
                +"unsplit-original-locals ";
    
        if( phaseName.equals( "bop" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "tag" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "tag.ln" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "tag.an" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "tag.dep" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "tag.fieldrw" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "db" ) )
            return ""
                +"enabled "
                +"source-is-javac ";
    
        if( phaseName.equals( "db.transformations" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "db.renamer" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "db.deobfuscate" ) )
            return ""
                +"enabled ";
    
        if( phaseName.equals( "db.force-recompile" ) )
            return ""
                +"enabled ";
    
        // The default set of options is just enabled.
        return "enabled";
    }

    public static String getDefaultOptionsForPhase( String phaseName ) {
    
        if( phaseName.equals( "jb" ) )
            return ""
              +"enabled:true "
              +"use-original-names:false "
              +"preserve-source-annotations:false ";
    
        if( phaseName.equals( "jb.ls" ) )
            return ""
              +"enabled:true ";
    
        if( phaseName.equals( "jb.a" ) )
            return ""
              +"enabled:true "
              +"only-stack-locals:true ";
    
        if( phaseName.equals( "jb.ule" ) )
            return ""
              +"enabled:true ";
    
        if( phaseName.equals( "jb.tr" ) )
            return ""
              +"enabled:true "
              +"ignore-wrong-staticness:false "
              +"use-older-type-assigner:false "
              +"compare-type-assigners:false ";
    
        if( phaseName.equals( "jb.ulp" ) )
            return ""
              +"enabled:true "
              +"unsplit-original-locals:true ";
    
        if( phaseName.equals( "jb.lns" ) )
            return ""
              +"enabled:true "
              +"only-stack-locals:false ";
    
        if( phaseName.equals( "jb.cp" ) )
            return ""
              +"enabled:true "
              +"only-regular-locals:false "
              +"only-stack-locals:true ";
    
        if( phaseName.equals( "jb.dae" ) )
            return ""
              +"enabled:true "
              +"only-stack-locals:true ";
    
        if( phaseName.equals( "jb.cp-ule" ) )
            return ""
              +"enabled:true ";
    
        if( phaseName.equals( "jb.lp" ) )
            return ""
              +"enabled:false "
              +"unsplit-original-locals:false ";
    
        if( phaseName.equals( "jb.ne" ) )
            return ""
              +"enabled:true ";
    
        if( phaseName.equals( "jb.uce" ) )
            return ""
              +"enabled:true "
              +"remove-unreachable-traps:false ";
    
        if( phaseName.equals( "jb.tt" ) )
            return ""
              +"enabled:false ";
    
        if( phaseName.equals( "jj" ) )
            return ""
              +"enabled:true "
              +"use-original-names:true ";
    
        if( phaseName.equals( "jj.ls" ) )
            return ""
              +"enabled:false ";
    
        if( phaseName.equals( "jj.a" ) )
            return ""
              +"enabled:true "
              +"only-stack-locals:true ";
    
        if( phaseName.equals( "jj.ule" ) )
            return ""
              +"enabled:true ";
    
        if( phaseName.equals( "jj.tr" ) )
            return ""
              +"enabled:false ";
    
        if( phaseName.equals( "jj.ulp" ) )
            return ""
              +"enabled:false "
              +"unsplit-original-locals:false ";
    
        if( phaseName.equals( "jj.lns" ) )
            return ""
              +"enabled:true "
              +"only-stack-locals:false ";
    
        if( phaseName.equals( "jj.cp" ) )
            return ""
              +"enabled:true "
              +"only-regular-locals:false "
              +"only-stack-locals:true ";
    
        if( phaseName.equals( "jj.dae" ) )
            return ""
              +"enabled:true "
              +"only-stack-locals:true ";
    
        if( phaseName.equals( "jj.cp-ule" ) )
            return ""
              +"enabled:true ";
    
        if( phaseName.equals( "jj.lp" ) )
            return ""
              +"enabled:false "
              +"unsplit-original-locals:false ";
    
        if( phaseName.equals( "jj.ne" ) )
            return ""
              +"enabled:true ";
    
        if( phaseName.equals( "jj.uce" ) )
            return ""
              +"enabled:true ";
    
        if( phaseName.equals( "wjpp" ) )
            return ""
              +"enabled:true ";
    
        if( phaseName.equals( "wspp" ) )
            return ""
              +"enabled:true ";
    
        if( phaseName.equals( "cg" ) )
            return ""
              +"enabled:true "
              +"safe-forname:false "
              +"safe-newinstance:false "
              +"verbose:false "
              +"jdkver:3 "
              +"all-reachable:false "
              +"implicit-entry:true "
              +"trim-clinit:true "
              +"guards:ignore ";
    
        if( phaseName.equals( "cg.cha" ) )
            return ""
              +"enabled:true "
              +"verbose:false ";
    
        if( phaseName.equals( "cg.spark" ) )
            return ""
              +"enabled:false "
              +"verbose:false "
              +"ignore-types:false "
              +"force-gc:false "
              +"pre-jimplify:false "
              +"vta:false "
              +"rta:false "
              +"field-based:false "
              +"types-for-sites:false "
              +"merge-stringbuffer:true "
              +"string-constants:false "
              +"simulate-natives:true "
              +"empties-as-allocs:false "
              +"simple-edges-bidirectional:false "
              +"on-fly-cg:true "
              +"simplify-offline:false "
              +"simplify-sccs:false "
              +"ignore-types-for-sccs:false "
              +"propagator:worklist "
              +"set-impl:double "
              +"double-set-old:hybrid "
              +"double-set-new:hybrid "
              +"dump-html:false "
              +"dump-pag:false "
              +"dump-solution:false "
              +"topo-sort:false "
              +"dump-types:true "
              +"class-method-var:true "
              +"dump-answer:false "
              +"add-tags:false "
              +"set-mass:false "
              +"cs-demand:false "
              +"lazy-pts:true "
              +"traversal:75000 "
              +"passes:10 "
              +"geom-pta:false "
              +"geom-encoding:Geom "
              +"geom-encoding:Geom "
              +"geom-worklist:PQ "
              +"geom-worklist:PQ "
              +"geom-dump-verbose: "
              +"geom-verify-name: "
              +"geom-eval:0 "
              +"geom-trans:false "
              +"geom-frac-base:40 "
              +"geom-blocking:true "
              +"geom-runs:1 "
              +"kobjsens:0 "
              +"kobjsens-context-for-static-inits:false "
              +"kobjsens-types-for-context:false "
              +"kobjsens-extra-array-context:false "
              +"kobjsens-no-context-list: "
              +"kobjsens-limit-heap-context: ";
    
        if( phaseName.equals( "cg.paddle" ) )
            return ""
              +"enabled:false "
              +"verbose:false "
              +"conf:ofcg "
              +"bdd:false "
              +"order:32 "
              +"profile:false "
              +"verbosegc:false "
              +"q:auto "
              +"backend:auto "
              +"bdd-nodes:0 "
              +"ignore-types:false "
              +"pre-jimplify:false "
              +"context:insens "
              +"k:2 "
              +"context-heap:false "
              +"rta:false "
              +"field-based:false "
              +"types-for-sites:false "
              +"merge-stringbuffer:true "
              +"string-constants:false "
              +"simulate-natives:true "
              +"global-nodes-in-natives:false "
              +"simple-edges-bidirectional:false "
              +"this-edges:false "
              +"precise-newinstance:true "
              +"propagator:auto "
              +"set-impl:double "
              +"double-set-old:hybrid "
              +"double-set-new:hybrid "
              +"context-counts:false "
              +"total-context-counts:false "
              +"method-context-counts:false "
              +"set-mass:false "
              +"number-nodes:true ";
    
        if( phaseName.equals( "wstp" ) )
            return ""
              +"enabled:true ";
    
        if( phaseName.equals( "wsop" ) )
            return ""
              +"enabled:false ";
    
        if( phaseName.equals( "wjtp" ) )
            return ""
              +"enabled:true ";
    
        if( phaseName.equals( "wjtp.mhp" ) )
            return ""
              +"enabled:false ";
    
        if( phaseName.equals( "wjtp.tn" ) )
            return ""
              +"enabled:false "
              +"locking-scheme:medium-grained "
              +"avoid-deadlock:true "
              +"open-nesting:true "
              +"do-mhp:true "
              +"do-tlo:true "
              +"print-graph:false "
              +"print-table:false "
              +"print-debug:false ";
    
        if( phaseName.equals( "wjtp.rdc" ) )
            return ""
              +"enabled:false ";
    
        if( phaseName.equals( "wjop" ) )
            return ""
              +"enabled:false ";
    
        if( phaseName.equals( "wjop.smb" ) )
            return ""
              +"enabled:false "
              +"insert-null-checks:true "
              +"insert-redundant-casts:true "
              +"allowed-modifier-changes:unsafe ";
    
        if( phaseName.equals( "wjop.si" ) )
            return ""
              +"enabled:true "
              +"rerun-jb:true "
              +"insert-null-checks:true "
              +"insert-redundant-casts:true "
              +"allowed-modifier-changes:unsafe "
              +"expansion-factor:3 "
              +"max-container-size:5000 "
              +"max-inlinee-size:20 ";
    
        if( phaseName.equals( "wjap" ) )
            return ""
              +"enabled:true ";
    
        if( phaseName.equals( "wjap.ra" ) )
            return ""
              +"enabled:false ";
    
        if( phaseName.equals( "wjap.umt" ) )
            return ""
              +"enabled:false ";
    
        if( phaseName.equals( "wjap.uft" ) )
            return ""
              +"enabled:false ";
    
        if( phaseName.equals( "wjap.tqt" ) )
            return ""
              +"enabled:false ";
    
        if( phaseName.equals( "wjap.cgg" ) )
            return ""
              +"enabled:false "
              +"show-lib-meths:false ";
    
        if( phaseName.equals( "wjap.purity" ) )
            return ""
              +"enabled:false "
              +"dump-summaries:true "
              +"dump-cg:false "
              +"dump-intra:false "
              +"print:true "
              +"annotate:true "
              +"verbose:false ";
    
        if( phaseName.equals( "shimple" ) )
            return ""
              +"enabled:true "
              +"node-elim-opt:true "
              +"standard-local-names:false "
              +"extended:false "
              +"debug:false ";
    
        if( phaseName.equals( "stp" ) )
            return ""
              +"enabled:true ";
    
        if( phaseName.equals( "sop" ) )
            return ""
              +"enabled:false ";
    
        if( phaseName.equals( "sop.cpf" ) )
            return ""
              +"enabled:true "
              +"prune-cfg:true ";
    
        if( phaseName.equals( "jtp" ) )
            return ""
              +"enabled:true ";
    
        if( phaseName.equals( "jop" ) )
            return ""
              +"enabled:false ";
    
        if( phaseName.equals( "jop.cse" ) )
            return ""
              +"enabled:false "
              +"naive-side-effect:false ";
    
        if( phaseName.equals( "jop.bcm" ) )
            return ""
              +"enabled:false "
              +"naive-side-effect:false ";
    
        if( phaseName.equals( "jop.lcm" ) )
            return ""
              +"enabled:false "
              +"safety:safe "
              +"unroll:true "
              +"naive-side-effect:false ";
    
        if( phaseName.equals( "jop.cp" ) )
            return ""
              +"enabled:true "
              +"only-regular-locals:false "
              +"only-stack-locals:false ";
    
        if( phaseName.equals( "jop.cpf" ) )
            return ""
              +"enabled:true ";
    
        if( phaseName.equals( "jop.cbf" ) )
            return ""
              +"enabled:true ";
    
        if( phaseName.equals( "jop.dae" ) )
            return ""
              +"enabled:true "
              +"only-tag:false "
              +"only-stack-locals:false ";
    
        if( phaseName.equals( "jop.nce" ) )
            return ""
              +"enabled:false ";
    
        if( phaseName.equals( "jop.uce1" ) )
            return ""
              +"enabled:true "
              +"remove-unreachable-traps:false ";
    
        if( phaseName.equals( "jop.ubf1" ) )
            return ""
              +"enabled:true ";
    
        if( phaseName.equals( "jop.uce2" ) )
            return ""
              +"enabled:true "
              +"remove-unreachable-traps:false ";
    
        if( phaseName.equals( "jop.ubf2" ) )
            return ""
              +"enabled:true ";
    
        if( phaseName.equals( "jop.ule" ) )
            return ""
              +"enabled:true ";
    
        if( phaseName.equals( "jap" ) )
            return ""
              +"enabled:true ";
    
        if( phaseName.equals( "jap.npc" ) )
            return ""
              +"enabled:false "
              +"only-array-ref:false "
              +"profiling:false ";
    
        if( phaseName.equals( "jap.npcolorer" ) )
            return ""
              +"enabled:false ";
    
        if( phaseName.equals( "jap.abc" ) )
            return ""
              +"enabled:false "
              +"with-all:false "
              +"with-cse:false "
              +"with-arrayref:false "
              +"with-fieldref:false "
              +"with-classfield:false "
              +"with-rectarray:false "
              +"profiling:false "
              +"add-color-tags:false ";
    
        if( phaseName.equals( "jap.profiling" ) )
            return ""
              +"enabled:false "
              +"notmainentry:false ";
    
        if( phaseName.equals( "jap.sea" ) )
            return ""
              +"enabled:false "
              +"naive:false ";
    
        if( phaseName.equals( "jap.fieldrw" ) )
            return ""
              +"enabled:false "
              +"threshold:100 ";
    
        if( phaseName.equals( "jap.cgtagger" ) )
            return ""
              +"enabled:false ";
    
        if( phaseName.equals( "jap.parity" ) )
            return ""
              +"enabled:false ";
    
        if( phaseName.equals( "jap.pat" ) )
            return ""
              +"enabled:false ";
    
        if( phaseName.equals( "jap.lvtagger" ) )
            return ""
              +"enabled:false ";
    
        if( phaseName.equals( "jap.rdtagger" ) )
            return ""
              +"enabled:false ";
    
        if( phaseName.equals( "jap.che" ) )
            return ""
              +"enabled:false ";
    
        if( phaseName.equals( "jap.umt" ) )
            return ""
              +"enabled:false ";
    
        if( phaseName.equals( "jap.lit" ) )
            return ""
              +"enabled:false ";
    
        if( phaseName.equals( "jap.aet" ) )
            return ""
              +"enabled:false "
              +"kind:optimistic ";
    
        if( phaseName.equals( "jap.dmt" ) )
            return ""
              +"enabled:false ";
    
        if( phaseName.equals( "gb" ) )
            return ""
              +"enabled:true ";
    
        if( phaseName.equals( "gb.a1" ) )
            return ""
              +"enabled:true "
              +"only-stack-locals:true ";
    
        if( phaseName.equals( "gb.cf" ) )
            return ""
              +"enabled:true ";
    
        if( phaseName.equals( "gb.a2" ) )
            return ""
              +"enabled:true "
              +"only-stack-locals:true ";
    
        if( phaseName.equals( "gb.ule" ) )
            return ""
              +"enabled:true ";
    
        if( phaseName.equals( "gop" ) )
            return ""
              +"enabled:false ";
    
        if( phaseName.equals( "bb" ) )
            return ""
              +"enabled:true ";
    
        if( phaseName.equals( "bb.lso" ) )
            return ""
              +"enabled:true "
              +"debug:false "
              +"inter:false "
              +"sl:true "
              +"sl2:false "
              +"sll:true "
              +"sll2:false ";
    
        if( phaseName.equals( "bb.pho" ) )
            return ""
              +"enabled:true ";
    
        if( phaseName.equals( "bb.ule" ) )
            return ""
              +"enabled:true ";
    
        if( phaseName.equals( "bb.lp" ) )
            return ""
              +"enabled:true "
              +"unsplit-original-locals:false ";
    
        if( phaseName.equals( "bop" ) )
            return ""
              +"enabled:false ";
    
        if( phaseName.equals( "tag" ) )
            return ""
              +"enabled:true ";
    
        if( phaseName.equals( "tag.ln" ) )
            return ""
              +"enabled:true ";
    
        if( phaseName.equals( "tag.an" ) )
            return ""
              +"enabled:false ";
    
        if( phaseName.equals( "tag.dep" ) )
            return ""
              +"enabled:false ";
    
        if( phaseName.equals( "tag.fieldrw" ) )
            return ""
              +"enabled:false ";
    
        if( phaseName.equals( "db" ) )
            return ""
              +"enabled:true "
              +"source-is-javac:true ";
    
        if( phaseName.equals( "db.transformations" ) )
            return ""
              +"enabled:true ";
    
        if( phaseName.equals( "db.renamer" ) )
            return ""
              +"enabled:false ";
    
        if( phaseName.equals( "db.deobfuscate" ) )
            return ""
              +"enabled:true ";
    
        if( phaseName.equals( "db.force-recompile" ) )
            return ""
              +"enabled:true ";
    
        // The default default value is enabled.
        return "enabled";
    }
  
    public void warnForeignPhase( String phaseName ) {
    
        if( phaseName.equals( "jb" ) ) return;
        if( phaseName.equals( "jb.ls" ) ) return;
        if( phaseName.equals( "jb.a" ) ) return;
        if( phaseName.equals( "jb.ule" ) ) return;
        if( phaseName.equals( "jb.tr" ) ) return;
        if( phaseName.equals( "jb.ulp" ) ) return;
        if( phaseName.equals( "jb.lns" ) ) return;
        if( phaseName.equals( "jb.cp" ) ) return;
        if( phaseName.equals( "jb.dae" ) ) return;
        if( phaseName.equals( "jb.cp-ule" ) ) return;
        if( phaseName.equals( "jb.lp" ) ) return;
        if( phaseName.equals( "jb.ne" ) ) return;
        if( phaseName.equals( "jb.uce" ) ) return;
        if( phaseName.equals( "jb.tt" ) ) return;
        if( phaseName.equals( "jj" ) ) return;
        if( phaseName.equals( "jj.ls" ) ) return;
        if( phaseName.equals( "jj.a" ) ) return;
        if( phaseName.equals( "jj.ule" ) ) return;
        if( phaseName.equals( "jj.tr" ) ) return;
        if( phaseName.equals( "jj.ulp" ) ) return;
        if( phaseName.equals( "jj.lns" ) ) return;
        if( phaseName.equals( "jj.cp" ) ) return;
        if( phaseName.equals( "jj.dae" ) ) return;
        if( phaseName.equals( "jj.cp-ule" ) ) return;
        if( phaseName.equals( "jj.lp" ) ) return;
        if( phaseName.equals( "jj.ne" ) ) return;
        if( phaseName.equals( "jj.uce" ) ) return;
        if( phaseName.equals( "wjpp" ) ) return;
        if( phaseName.equals( "wspp" ) ) return;
        if( phaseName.equals( "cg" ) ) return;
        if( phaseName.equals( "cg.cha" ) ) return;
        if( phaseName.equals( "cg.spark" ) ) return;
        if( phaseName.equals( "cg.paddle" ) ) return;
        if( phaseName.equals( "wstp" ) ) return;
        if( phaseName.equals( "wsop" ) ) return;
        if( phaseName.equals( "wjtp" ) ) return;
        if( phaseName.equals( "wjtp.mhp" ) ) return;
        if( phaseName.equals( "wjtp.tn" ) ) return;
        if( phaseName.equals( "wjtp.rdc" ) ) return;
        if( phaseName.equals( "wjop" ) ) return;
        if( phaseName.equals( "wjop.smb" ) ) return;
        if( phaseName.equals( "wjop.si" ) ) return;
        if( phaseName.equals( "wjap" ) ) return;
        if( phaseName.equals( "wjap.ra" ) ) return;
        if( phaseName.equals( "wjap.umt" ) ) return;
        if( phaseName.equals( "wjap.uft" ) ) return;
        if( phaseName.equals( "wjap.tqt" ) ) return;
        if( phaseName.equals( "wjap.cgg" ) ) return;
        if( phaseName.equals( "wjap.purity" ) ) return;
        if( phaseName.equals( "shimple" ) ) return;
        if( phaseName.equals( "stp" ) ) return;
        if( phaseName.equals( "sop" ) ) return;
        if( phaseName.equals( "sop.cpf" ) ) return;
        if( phaseName.equals( "jtp" ) ) return;
        if( phaseName.equals( "jop" ) ) return;
        if( phaseName.equals( "jop.cse" ) ) return;
        if( phaseName.equals( "jop.bcm" ) ) return;
        if( phaseName.equals( "jop.lcm" ) ) return;
        if( phaseName.equals( "jop.cp" ) ) return;
        if( phaseName.equals( "jop.cpf" ) ) return;
        if( phaseName.equals( "jop.cbf" ) ) return;
        if( phaseName.equals( "jop.dae" ) ) return;
        if( phaseName.equals( "jop.nce" ) ) return;
        if( phaseName.equals( "jop.uce1" ) ) return;
        if( phaseName.equals( "jop.ubf1" ) ) return;
        if( phaseName.equals( "jop.uce2" ) ) return;
        if( phaseName.equals( "jop.ubf2" ) ) return;
        if( phaseName.equals( "jop.ule" ) ) return;
        if( phaseName.equals( "jap" ) ) return;
        if( phaseName.equals( "jap.npc" ) ) return;
        if( phaseName.equals( "jap.npcolorer" ) ) return;
        if( phaseName.equals( "jap.abc" ) ) return;
        if( phaseName.equals( "jap.profiling" ) ) return;
        if( phaseName.equals( "jap.sea" ) ) return;
        if( phaseName.equals( "jap.fieldrw" ) ) return;
        if( phaseName.equals( "jap.cgtagger" ) ) return;
        if( phaseName.equals( "jap.parity" ) ) return;
        if( phaseName.equals( "jap.pat" ) ) return;
        if( phaseName.equals( "jap.lvtagger" ) ) return;
        if( phaseName.equals( "jap.rdtagger" ) ) return;
        if( phaseName.equals( "jap.che" ) ) return;
        if( phaseName.equals( "jap.umt" ) ) return;
        if( phaseName.equals( "jap.lit" ) ) return;
        if( phaseName.equals( "jap.aet" ) ) return;
        if( phaseName.equals( "jap.dmt" ) ) return;
        if( phaseName.equals( "gb" ) ) return;
        if( phaseName.equals( "gb.a1" ) ) return;
        if( phaseName.equals( "gb.cf" ) ) return;
        if( phaseName.equals( "gb.a2" ) ) return;
        if( phaseName.equals( "gb.ule" ) ) return;
        if( phaseName.equals( "gop" ) ) return;
        if( phaseName.equals( "bb" ) ) return;
        if( phaseName.equals( "bb.lso" ) ) return;
        if( phaseName.equals( "bb.pho" ) ) return;
        if( phaseName.equals( "bb.ule" ) ) return;
        if( phaseName.equals( "bb.lp" ) ) return;
        if( phaseName.equals( "bop" ) ) return;
        if( phaseName.equals( "tag" ) ) return;
        if( phaseName.equals( "tag.ln" ) ) return;
        if( phaseName.equals( "tag.an" ) ) return;
        if( phaseName.equals( "tag.dep" ) ) return;
        if( phaseName.equals( "tag.fieldrw" ) ) return;
        if( phaseName.equals( "db" ) ) return;
        if( phaseName.equals( "db.transformations" ) ) return;
        if( phaseName.equals( "db.renamer" ) ) return;
        if( phaseName.equals( "db.deobfuscate" ) ) return;
        if( phaseName.equals( "db.force-recompile" ) ) return;
        G.v().out.println( "Warning: Phase "+phaseName+" is not a standard Soot phase listed in XML files." );
    }

    public void warnNonexistentPhase() {
    
        if( !PackManager.v().hasPhase( "jb" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jb" );
        if( !PackManager.v().hasPhase( "jb.ls" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jb.ls" );
        if( !PackManager.v().hasPhase( "jb.a" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jb.a" );
        if( !PackManager.v().hasPhase( "jb.ule" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jb.ule" );
        if( !PackManager.v().hasPhase( "jb.tr" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jb.tr" );
        if( !PackManager.v().hasPhase( "jb.ulp" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jb.ulp" );
        if( !PackManager.v().hasPhase( "jb.lns" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jb.lns" );
        if( !PackManager.v().hasPhase( "jb.cp" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jb.cp" );
        if( !PackManager.v().hasPhase( "jb.dae" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jb.dae" );
        if( !PackManager.v().hasPhase( "jb.cp-ule" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jb.cp-ule" );
        if( !PackManager.v().hasPhase( "jb.lp" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jb.lp" );
        if( !PackManager.v().hasPhase( "jb.ne" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jb.ne" );
        if( !PackManager.v().hasPhase( "jb.uce" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jb.uce" );
        if( !PackManager.v().hasPhase( "jb.tt" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jb.tt" );
        if( !PackManager.v().hasPhase( "jj" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jj" );
        if( !PackManager.v().hasPhase( "jj.ls" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jj.ls" );
        if( !PackManager.v().hasPhase( "jj.a" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jj.a" );
        if( !PackManager.v().hasPhase( "jj.ule" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jj.ule" );
        if( !PackManager.v().hasPhase( "jj.tr" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jj.tr" );
        if( !PackManager.v().hasPhase( "jj.ulp" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jj.ulp" );
        if( !PackManager.v().hasPhase( "jj.lns" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jj.lns" );
        if( !PackManager.v().hasPhase( "jj.cp" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jj.cp" );
        if( !PackManager.v().hasPhase( "jj.dae" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jj.dae" );
        if( !PackManager.v().hasPhase( "jj.cp-ule" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jj.cp-ule" );
        if( !PackManager.v().hasPhase( "jj.lp" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jj.lp" );
        if( !PackManager.v().hasPhase( "jj.ne" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jj.ne" );
        if( !PackManager.v().hasPhase( "jj.uce" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jj.uce" );
        if( !PackManager.v().hasPhase( "wjpp" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase wjpp" );
        if( !PackManager.v().hasPhase( "wspp" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase wspp" );
        if( !PackManager.v().hasPhase( "cg" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase cg" );
        if( !PackManager.v().hasPhase( "cg.cha" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase cg.cha" );
        if( !PackManager.v().hasPhase( "cg.spark" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase cg.spark" );
        if( !PackManager.v().hasPhase( "cg.paddle" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase cg.paddle" );
        if( !PackManager.v().hasPhase( "wstp" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase wstp" );
        if( !PackManager.v().hasPhase( "wsop" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase wsop" );
        if( !PackManager.v().hasPhase( "wjtp" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase wjtp" );
        if( !PackManager.v().hasPhase( "wjtp.mhp" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase wjtp.mhp" );
        if( !PackManager.v().hasPhase( "wjtp.tn" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase wjtp.tn" );
        if( !PackManager.v().hasPhase( "wjtp.rdc" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase wjtp.rdc" );
        if( !PackManager.v().hasPhase( "wjop" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase wjop" );
        if( !PackManager.v().hasPhase( "wjop.smb" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase wjop.smb" );
        if( !PackManager.v().hasPhase( "wjop.si" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase wjop.si" );
        if( !PackManager.v().hasPhase( "wjap" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase wjap" );
        if( !PackManager.v().hasPhase( "wjap.ra" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase wjap.ra" );
        if( !PackManager.v().hasPhase( "wjap.umt" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase wjap.umt" );
        if( !PackManager.v().hasPhase( "wjap.uft" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase wjap.uft" );
        if( !PackManager.v().hasPhase( "wjap.tqt" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase wjap.tqt" );
        if( !PackManager.v().hasPhase( "wjap.cgg" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase wjap.cgg" );
        if( !PackManager.v().hasPhase( "wjap.purity" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase wjap.purity" );
        if( !PackManager.v().hasPhase( "shimple" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase shimple" );
        if( !PackManager.v().hasPhase( "stp" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase stp" );
        if( !PackManager.v().hasPhase( "sop" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase sop" );
        if( !PackManager.v().hasPhase( "sop.cpf" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase sop.cpf" );
        if( !PackManager.v().hasPhase( "jtp" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jtp" );
        if( !PackManager.v().hasPhase( "jop" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jop" );
        if( !PackManager.v().hasPhase( "jop.cse" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jop.cse" );
        if( !PackManager.v().hasPhase( "jop.bcm" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jop.bcm" );
        if( !PackManager.v().hasPhase( "jop.lcm" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jop.lcm" );
        if( !PackManager.v().hasPhase( "jop.cp" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jop.cp" );
        if( !PackManager.v().hasPhase( "jop.cpf" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jop.cpf" );
        if( !PackManager.v().hasPhase( "jop.cbf" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jop.cbf" );
        if( !PackManager.v().hasPhase( "jop.dae" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jop.dae" );
        if( !PackManager.v().hasPhase( "jop.nce" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jop.nce" );
        if( !PackManager.v().hasPhase( "jop.uce1" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jop.uce1" );
        if( !PackManager.v().hasPhase( "jop.ubf1" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jop.ubf1" );
        if( !PackManager.v().hasPhase( "jop.uce2" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jop.uce2" );
        if( !PackManager.v().hasPhase( "jop.ubf2" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jop.ubf2" );
        if( !PackManager.v().hasPhase( "jop.ule" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jop.ule" );
        if( !PackManager.v().hasPhase( "jap" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jap" );
        if( !PackManager.v().hasPhase( "jap.npc" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jap.npc" );
        if( !PackManager.v().hasPhase( "jap.npcolorer" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jap.npcolorer" );
        if( !PackManager.v().hasPhase( "jap.abc" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jap.abc" );
        if( !PackManager.v().hasPhase( "jap.profiling" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jap.profiling" );
        if( !PackManager.v().hasPhase( "jap.sea" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jap.sea" );
        if( !PackManager.v().hasPhase( "jap.fieldrw" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jap.fieldrw" );
        if( !PackManager.v().hasPhase( "jap.cgtagger" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jap.cgtagger" );
        if( !PackManager.v().hasPhase( "jap.parity" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jap.parity" );
        if( !PackManager.v().hasPhase( "jap.pat" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jap.pat" );
        if( !PackManager.v().hasPhase( "jap.lvtagger" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jap.lvtagger" );
        if( !PackManager.v().hasPhase( "jap.rdtagger" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jap.rdtagger" );
        if( !PackManager.v().hasPhase( "jap.che" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jap.che" );
        if( !PackManager.v().hasPhase( "jap.umt" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jap.umt" );
        if( !PackManager.v().hasPhase( "jap.lit" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jap.lit" );
        if( !PackManager.v().hasPhase( "jap.aet" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jap.aet" );
        if( !PackManager.v().hasPhase( "jap.dmt" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase jap.dmt" );
        if( !PackManager.v().hasPhase( "gb" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase gb" );
        if( !PackManager.v().hasPhase( "gb.a1" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase gb.a1" );
        if( !PackManager.v().hasPhase( "gb.cf" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase gb.cf" );
        if( !PackManager.v().hasPhase( "gb.a2" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase gb.a2" );
        if( !PackManager.v().hasPhase( "gb.ule" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase gb.ule" );
        if( !PackManager.v().hasPhase( "gop" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase gop" );
        if( !PackManager.v().hasPhase( "bb" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase bb" );
        if( !PackManager.v().hasPhase( "bb.lso" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase bb.lso" );
        if( !PackManager.v().hasPhase( "bb.pho" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase bb.pho" );
        if( !PackManager.v().hasPhase( "bb.ule" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase bb.ule" );
        if( !PackManager.v().hasPhase( "bb.lp" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase bb.lp" );
        if( !PackManager.v().hasPhase( "bop" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase bop" );
        if( !PackManager.v().hasPhase( "tag" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase tag" );
        if( !PackManager.v().hasPhase( "tag.ln" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase tag.ln" );
        if( !PackManager.v().hasPhase( "tag.an" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase tag.an" );
        if( !PackManager.v().hasPhase( "tag.dep" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase tag.dep" );
        if( !PackManager.v().hasPhase( "tag.fieldrw" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase tag.fieldrw" );
        if( !PackManager.v().hasPhase( "db" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase db" );
        if( !PackManager.v().hasPhase( "db.transformations" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase db.transformations" );
        if( !PackManager.v().hasPhase( "db.renamer" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase db.renamer" );
        if( !PackManager.v().hasPhase( "db.deobfuscate" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase db.deobfuscate" );
        if( !PackManager.v().hasPhase( "db.force-recompile" ) )
            G.v().out.println( "Warning: Options exist for non-existent phase db.force-recompile" );
    }
  
}

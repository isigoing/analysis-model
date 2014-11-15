package hudson.plugins.ast.factory;

import hudson.plugins.analysis.util.model.FileAnnotation;
import hudson.plugins.ast.specific.DefaultAst;
import hudson.plugins.ast.specific.JavadocMethodCheckAst;
import hudson.plugins.ast.specific.StrictDuplicateCodeCheckAst;

/**
 * Factory for abstract syntax trees.
 *
 * @author Christian M&ouml;stl
 */
public final class AstFactory {

    private AstFactory() {
        // prevents initialization
    }

    private static final String[] METHOD_AST = new String[]{"AnonInnerLength", "CovariantEquals", "EqualsAvoidNull",
            "EqualsHashCode", "HiddenField", "JavadocMethod", "MethodLength", "MethodParamPad", "ParameterAssignment",
            "ParameterNumber", "SuperClone", "SuperFinalize", "ThrowsCount"};
    private static final String[] ENVIRONMENT_AST = new String[]{"ArrayTypeStyle", "AvoidNestedBlocks",
            "BooleanExpressionComplexity", "DefaultComesLast", "EmptyBlock", "EmptyForIteratorPad", "EmptyStatement",
            "FallThrough", "GenericWhitespace", "IllegalCatch", "IllegalThrows", "InnerAssignment", "JavaNCSS",
            "LeftCurly", "MissingSwitchDefault", "ModifiedControlVariable", "MultipleVariableDeclarations",
            "NeedBraces", "NestedForDepth", "NestedIfDepth", "NestedTryDepth", "NoWhitespaceAfter",
            "NoWhitespaceBefore", "NPathComplexity", "OneStatementPerLine", "OperatorWrap", "ParenPad", "RightCurly",
            "SimplifyBooleanExpression", "SimplifyBooleanReturn", "StringLiteralEquality", "TypecastParenPad",
            "UnnecessaryParentheses", "UpperEll", "WhitespaceAfter", "WhitespaceAround"};
    private static final String[] FILE_AST = new String[]{"ClassDataAbstractionCoupling", "ClassFanOutComplexity",
            "FileLength", "IllegalImport", "InterfaceIsType", "OuterTypeFilename", "PackageDeclaration"};
    private static final String[] CLASS_AST = new String[]{"FinalClass", "HideUtilityClassConstructor",
            "InnerTypeLast", "JavadocType", "JUnitTestCase", "MultipleStringLiterals", "MutableException"};
    private static final String[] METHOD_OR_CLASS_AST = new String[]{"AnnotationUseStyle", "FileTabCharacter",
            "JavadocStyle", "MissingDeprecated", "ModifierOrder", "RedundantModifier", "VisibilityModifier"};
    private static final String[] INSTANCEVARIABLE_AST = new String[]{"ConstantName", "ExplicitInitialization",
            "JavadocVariable"};

    private static final String[] NAME_ENVIRONMENT_AST = new String[]{"LocalFinalVariableName", "LocalVariableName"};

    private static final String[] NAME_INSTANCEVARIABLE_AST = new String[]{"MemberName", "StaticVariableName"};

    private static final String[] NAME_METHOD_AST = new String[]{"MethodName", "MethodTypeParameterName",
            "ParameterName"};

    private static final String[] NAME_CLASS_AST = new String[]{"ClassTypeParameterName", "TypeName"};

    private static final String[] NAME_PACKAGE_AST = new String[]{"PackageName"};

    private static final String[] QUESTION = new String[]{"FileContentsHolder", "PackageAnnotation"};

    /**
     * Creates an instance of a specific {@link Ast}.
     *
     * @param filename
     *            the file
     * @param fileAnnotation
     *            the {@link FileAnnotation}
     * @return the specific ast. TODO:
     */
    public static Ast getInstance2(final String filename, final FileAnnotation fileAnnotation) {
        String type = fileAnnotation.getType();
        Ast ast;
        // Notiz: switch case statement erst ab Java 1.7

        if ("JavadocMethodCheck".equals(type)) {
            ast = new JavadocMethodCheckAst(filename, fileAnnotation);
        }/*
          * else if ("com.puppycrawl.tools.checkstyle.checks.javadoc.JavadocPackageCheck".equals(type)) { /* Vergleiche
          * nur(!) Klassennamen im Package. (ohne AST lösen...) Wenn Klasse hinzugefügt wird, wird Warning
          * fälschlicherweise als neu deklariert. TODO }
          */
        else if ("com.puppycrawl.tools.checkstyle.checks.duplicates.StrictDuplicateCodeCheck".equals(type)) {
            ast = new StrictDuplicateCodeCheckAst(filename, fileAnnotation);
        }
        else {
            ast = new DefaultAst(filename, fileAnnotation);
        }
        return ast;
    }

    /**
     * Creates an instance of a specific {@link Ast}.
     *
     * @param filename
     *            the file
     * @param fileAnnotation
     *            the {@link FileAnnotation}
     * @return the specific ast.
     */
    public static Ast getInstance(final String filename, final FileAnnotation fileAnnotation) {
        String type = fileAnnotation.getType();
        Ast ast;
        // Notiz: switch case statement erst ab Java 1.7

        if ("JavadocMethodCheck".equals(type)) {
            ast = new JavadocMethodCheckAst(filename, fileAnnotation);
        }/*
          * else if ("com.puppycrawl.tools.checkstyle.checks.javadoc.JavadocPackageCheck".equals(type)) { /* Vergleiche
          * nur(!) Klassennamen im Package. (ohne AST lösen...) Wenn Klasse hinzugefügt wird, wird Warning
          * fälschlicherweise als neu deklariert. TODO }
          */
        else if ("com.puppycrawl.tools.checkstyle.checks.duplicates.StrictDuplicateCodeCheck".equals(type)) {
            ast = new StrictDuplicateCodeCheckAst(filename, fileAnnotation);
        }
        else {
            ast = new DefaultAst(filename, fileAnnotation);
        }
        return ast;
    }
}
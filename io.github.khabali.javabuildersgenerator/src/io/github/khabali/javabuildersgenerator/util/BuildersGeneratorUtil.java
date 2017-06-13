/**
 * MIT License
 *
 * Copyright (c) 2017 Anas KHABALI
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.github.khabali.javabuildersgenerator.util;

import java.util.List;

import io.github.khabali.javabuildersgenerator.Field;

public final class BuildersGeneratorUtil {

	public static String generateBuilderConstructor(final String classToBuildName, final List<Field> fields) {
		final StringBuilder sb = new StringBuilder();
		sb.append("public " + classToBuildName + ("(Builder builder) {\r\n"));
		for (final Field field : fields) {
			sb.append("\tthis." + field.getName() + " = builder." + field.getName() + ";\r\n");
		}
		sb.append("}");
		return sb.toString();
	}

	/**
	 * This method generate the builder start point static method
	 *
	 * @param fields
	 * @return
	 */
	public static String generateStartPoint(final List<Field> fields) {
		final StringBuilder sb = new StringBuilder();
		if (fields == null || fields.size() == 0) {
			sb.append("public static  Builder  builder() {\r\n");
			sb.append("    return new Builder();\r\n");
			sb.append("}");
		} else {
			sb.append("public static " + toCamelCase(fields.get(0).getName()) + " builder() {\r\n");
			sb.append("    return new Builder();\r\n");
			sb.append("}");
		}

		return sb.toString();
	}

	/**
	 * This method generate global interface for non mandatory fields builder
	 *
	 *
	 * @param mandatory
	 * @return
	 */
	public static String generateMandatoryFieldInterface(final List<Field> mandatory) {
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < mandatory.size(); i++) {
			final Field current = mandatory.get(i);
			sb.append("interface " + toCamelCase(current.getName()) + " {\r\n");
			if (i + 1 < mandatory.size()) {
				final Field next = mandatory.get(i + 1);
				sb//
				        .append("\tpublic ")//
				        .append(toCamelCase(next.getName()))//
				        .append(" ")//
				        .append(current.getName())//
				        .append("(").append(current.getType()).append(" ").append(current.getName()).append(");\r\n");
			} else {
				sb//
				        .append("\tpublic Build ")//
				        .append(current.getName())//
				        .append("(").append(current.getType()).append(" ").append(current.getName()).append(");\r\n");
			}

			sb.append("}\r\n");
		}

		return sb.toString();
	}

	public static String generateBuildInterface(final String classToBuildQName, final List<Field> fields) {
		final StringBuilder sb = new StringBuilder();
		sb.append("interface Build {\r\n");
		for (final Field field : fields) {
			sb.append("    public Build " + field.getName() + "(" + field.getType() + " " + field.getName() + ");\n");
		}
		sb.append("    public " + classToBuildQName + " build();\n");
		sb.append("}");
		return sb.toString();
	}

	public static String generateBuilderImpl(final String mainClassName, final List<Field> mandatory,
	        final List<Field> optional) {

		final StringBuilder sb = new StringBuilder();
		sb.append("private static class Builder implements Build");
		for (final Field field : mandatory) {
			sb.append(", " + toCamelCase(field.getName()));
		}
		sb.append(" {\r\n");

		// attributes
		for (final Field field : mandatory) {
			sb.append("\tprivate " + field.getType() + " " + field.getName() + ";\r\n");
		}
		for (final Field field : optional) {
			sb.append("\tprivate " + field.getType() + " " + field.getName() + ";\r\n");
		}
		sb.append("\r\n");

		// methods
		for (final Field field : optional) {
			sb.append("\tpublic Build " + field.getName() + "(" + field.getType() + " " + field.getName() + "){\r\n");
			sb.append("\t\tthis." + field.getName() + " = " + field.getName() + ";\r\n");
			sb.append("\t\treturn this;\r\n");
			sb.append("\t}\r\n");
		}

		for (int i = 0; i < mandatory.size(); i++) {
			final Field current = mandatory.get(i);
			if (i + 1 < mandatory.size()) {
				final Field next = mandatory.get(i + 1);
				sb.append("\tpublic ");
				sb.append(toCamelCase(next.getName()));
			} else {
				sb.append("\tpublic Build");
			}
			sb.append(" ");
			sb.append(current.getName()).append("(").append(current.getType()).append(" ").append(current.getName());
			sb.append(") {\r\n");

			sb.append("\t\tthis.").append(current.getName()).append(" = ").append(current.getName()).append(";\r\n");
			sb.append("\t\treturn this;\r\n");
			sb.append("\t}\r\n");
		}
		sb.append("\r\n");
		sb.append("\tpublic " + mainClassName + " build(){\r\n");
		sb.append("\t\treturn new " + mainClassName + "(this);\r\n");
		sb.append("\t}");

		sb.append("\r\n}");
		return sb.toString();
	}

	private static String toCamelCase(final String s) {
		return s.toUpperCase().charAt(0) + s.substring(1);
	}

	private BuildersGeneratorUtil() {

	}

}

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
package io.github.khabali.javabuildersgenerator.wizard;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.wizard.Wizard;

import io.github.khabali.javabuildersgenerator.Field;
import io.github.khabali.javabuildersgenerator.util.BuildersGeneratorUtil;

public class BuilderGeneratorWizard extends Wizard {

	private final BuildersGeneratorWizardPage mainPage;
	private final ICompilationUnit icu;

	public BuilderGeneratorWizard(final ICompilationUnit icu, final List<Field> fields) {
		super();
		setNeedsProgressMonitor(false);
		this.mainPage = new BuildersGeneratorWizardPage(fields);
		this.icu = icu;
	}

	@Override
	public String getWindowTitle() {
		return "Generate Builders using Fields";
	}

	@Override
	public void addPages() {
		addPage(this.mainPage);
	}

	@Override
	public boolean performFinish() {
		generateBuilder();
		return true;
	}

	private void generateBuilder() {

		try {

			final IType type = this.icu.getTypes()[0];
			final String className = type.getElementName();
			final List<Field> fields = this.mainPage.getFields();

			final int[] mandatoryIndexs = this.mainPage.getSelectedFields();
			final List<Field> mandatoryFields = getMandatoryField(fields, mandatoryIndexs);
			final List<Field> optionalFields = getOptionalFields(fields, mandatoryIndexs);

			// -- Create Constructor builder
			type.createMethod(BuildersGeneratorUtil.generateBuilderConstructor(className, fields), null, false, null);

			// -- Create static builder method start point
			type.createMethod(BuildersGeneratorUtil.generateStartPoint(mandatoryFields), null, false, null);

			// -- Builder
			type.createType(BuildersGeneratorUtil.generateBuilderImpl(className, mandatoryFields, optionalFields), null,
			        true, null);

			// -- Builder interface for optional and mandatory fields
			if (mandatoryFields != null && mandatoryFields.size() != 0) {
				type.createType(BuildersGeneratorUtil.generateMandatoryFieldInterface(mandatoryFields), null, false, null);
			}
			type.createType(BuildersGeneratorUtil.generateBuildInterface(className, optionalFields), null, false, null);

		}
		catch (final JavaModelException e) {
			e.printStackTrace();
		}

	}

	private List<Field> getOptionalFields(final List<Field> fields, final int[] mandatoryIndexs) {
		final List<Field> res = new ArrayList<>(fields);
		res.removeAll(getMandatoryField(fields, mandatoryIndexs));
		return res;
	}

	private List<Field> getMandatoryField(final List<Field> fields, final int[] mandatoryIndexs) {
		final List<Field> res = new ArrayList<>();
		for (int i = 0; i < mandatoryIndexs.length; i++) {
			res.add(fields.get(mandatoryIndexs[i]));
		}
		return res;
	}

}

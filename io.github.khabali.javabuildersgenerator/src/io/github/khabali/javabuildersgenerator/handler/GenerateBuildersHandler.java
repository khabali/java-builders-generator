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
package io.github.khabali.javabuildersgenerator.handler;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;

import io.github.khabali.javabuildersgenerator.Field;
import io.github.khabali.javabuildersgenerator.wizard.BuilderGeneratorWizard;

public class GenerateBuildersHandler extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {

		final IEditorPart editorPart = HandlerUtil.getActiveEditor(event);
		final ICompilationUnit icu = JavaUI.getWorkingCopyManager().getWorkingCopy(editorPart.getEditorInput());

		try {
			final IType type = icu.getTypes()[0];
			final List<Field> fields = new ArrayList<>();
			for (final IField field : type.getFields()) {
				final String fieldName = field.getElementName();
				final String fieldType = Signature.getSignatureSimpleName(field.getTypeSignature());
				fields.add(new Field(fieldName, fieldType));
			}

			new WizardDialog(HandlerUtil.getActiveShell(event), new BuilderGeneratorWizard(icu, fields)).open();

		}
		catch (final JavaModelException e) {
			e.printStackTrace();
		}

		return null;
	}

}

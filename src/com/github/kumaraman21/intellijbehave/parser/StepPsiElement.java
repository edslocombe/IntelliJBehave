/*
 * Copyright 2011-12 Aman Kumar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.kumaraman21.intellijbehave.parser;

import static org.apache.commons.lang.StringUtils.trim;

import com.github.kumaraman21.intellijbehave.highlighter.StoryTokenType;
import com.github.kumaraman21.intellijbehave.resolver.StepPsiReference;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;

import org.jbehave.core.steps.StepType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StepPsiElement extends ASTWrapperPsiElement {
    private StepType stepType;

    public StepPsiElement(@NotNull ASTNode node, StepType stepType) {
        super(node);
        this.stepType = stepType;
    }

    @Override
    @NotNull
    public StepPsiReference getReference() {
        return new StepPsiReference(this);
    }

    public StepType getStepType() {
        return stepType;
    }

    public boolean isAndStep() {
        ASTNode keyword = getKeyword();
        return keyword != null && keyword.getElementType() == StoryTokenType.STEP_TYPE_AND;
    }

    @Nullable
    public ASTNode getKeyword() {
        return getNode().findChildByType(StoryTokenType.STEP_TYPES);
    }

    public String getStepText() {
        int offset = getStepTextOffset();
        String text = getText();

        if (offset <= 0 || offset >= text.length()) {
            return trim(text);
        } else {
            return trim(text.substring(offset));
        }
    }

    @Nullable
    public String getActualStepPrefix() {
        ASTNode keyword = getKeyword();
        if (keyword == null) { // that's weird!
            return null;
        }
        return keyword.getText();
    }

    public int getStepTextOffset() {
        String stepPrefix = getActualStepPrefix();
        return stepPrefix != null ? stepPrefix.length() + 1 : 0;
    }
}

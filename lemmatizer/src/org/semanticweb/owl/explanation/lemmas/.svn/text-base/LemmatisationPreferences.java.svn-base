package org.semanticweb.owl.explanation.lemmas;

import java.util.Map;
import java.util.Set;
import java.util.LinkedHashMap;
/*
 * Copyright (C) 2009, University of Manchester
 *
 * Modifications to the initial code base are copyright of their
 * respective authors, or their employers as appropriate.  Authorship
 * of the modifications may be determined from the ChangeLog placed at
 * the end of this file.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

/**
 * Author: Matthew Horridge<br>
 * The University of Manchester<br>
 * Information Management Group<br>
 * Date: 12-Oct-2009
 */
public class LemmatisationPreferences {

    private static LemmatisationPreferences instance = new LemmatisationPreferences();

    private int justificationSearchLimit = 5;

    private boolean useLaconicJustifications = true;

    private boolean useSubConcepts = true;

    private Map<LemmaType, Boolean> lemmaTypeStatusMap = new LinkedHashMap<LemmaType, Boolean>();

    private LemmatisationPreferences() {
        lemmaTypeStatusMap.put(LemmaType.CONTRAPOSED_SUBCLASS_OF, false);
        lemmaTypeStatusMap.put(LemmaType.NAMED_CLASS_CLASS_ASSERTION, true);
        lemmaTypeStatusMap.put(LemmaType.OBJECT_ALL_SUBCLASS_OF, true);
        lemmaTypeStatusMap.put(LemmaType.OBJECT_PROPERTY_ASSERTION, true);
        lemmaTypeStatusMap.put(LemmaType.OBJECT_PROPERTY_DOMAIN, true);
        lemmaTypeStatusMap.put(LemmaType.OBJECT_PROPERTY_RANGE, true);
        lemmaTypeStatusMap.put(LemmaType.OBJECT_SOME_CLASS_ASSERTION, true);
        lemmaTypeStatusMap.put(LemmaType.OBJECT_ALL_CLASS_ASSERTION, true);
        lemmaTypeStatusMap.put(LemmaType.OBJECT_SOME_SUBCLASS_OF, true);
        lemmaTypeStatusMap.put(LemmaType.SUB_OBJECT_PROPERTY_OF, true);
        lemmaTypeStatusMap.put(LemmaType.SUB_OBJECT_PROPERTY_OF_INVERSE, false);
        lemmaTypeStatusMap.put(LemmaType.INVERSE_SUB_OBJECT_PROPERTY_OF, false);
        lemmaTypeStatusMap.put(LemmaType.SUBCLASS_OF_NAMED_CLASS, true);
        lemmaTypeStatusMap.put(LemmaType.SUBCLASS_OF_OBJECT_ALL, true);
        lemmaTypeStatusMap.put(LemmaType.SUBCLASS_OF_OBJECT_ALL_NOT, true);
        lemmaTypeStatusMap.put(LemmaType.SUBCLASS_OF_OBJECT_HAS_VALUE, true);
        lemmaTypeStatusMap.put(LemmaType.SUBCLASS_OF_OBJECT_MAX_CARDINALITY, true);
        lemmaTypeStatusMap.put(LemmaType.SUBCLASS_OF_OBJECT_MIN_CARDINALITY, true);
        lemmaTypeStatusMap.put(LemmaType.SUBCLASS_OF_OBJECT_SOME, true);
        lemmaTypeStatusMap.put(LemmaType.SUBCLASS_OF_OBJECT_SOME_NOT, true);
        lemmaTypeStatusMap.put(LemmaType.TOP_BOT_SYNONYM, true);
        lemmaTypeStatusMap.put(LemmaType.DISJOINT_CLASSES, true);
        lemmaTypeStatusMap.put(LemmaType.LHS_CONJUNCTS, true);
    }

    public static LemmatisationPreferences getInstance() {
        return instance;
    }

    public boolean isUseLaconicJustifications() {
        return useLaconicJustifications;
    }

    public void setUseLaconicJustifications(boolean useLaconicJustifications) {
        this.useLaconicJustifications = useLaconicJustifications;
    }

    public int getJustificationSearchLimit() {
        return justificationSearchLimit;
    }

    public void setJustificationSearchLimit(int justificationSearchLimit) {
        this.justificationSearchLimit = justificationSearchLimit;
    }

    public boolean isUseSubConcepts() {
        return useSubConcepts;
    }

    public void setUseSubConcepts(boolean useSubConcepts) {
        this.useSubConcepts = useSubConcepts;
    }

    public Set<LemmaType> getLemmaTypes() {
        return lemmaTypeStatusMap.keySet();
    }

    public boolean isLemmaTypeEnabled(LemmaType type) {
        Boolean b = lemmaTypeStatusMap.get(type);
        return b != null && b;
    }

    public void setEnabled(LemmaType type, boolean enabled) {
        lemmaTypeStatusMap.put(type, enabled);
    }
}

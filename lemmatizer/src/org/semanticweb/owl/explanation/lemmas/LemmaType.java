package org.semanticweb.owl.explanation.lemmas;
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
public enum LemmaType {

    TOP_BOT_SYNONYM("TopBottomSynonyms"),

    CONTRAPOSED_SUBCLASS_OF("Contraposed SubClassOf"),

    SUBCLASS_OF_NAMED_CLASS("NamedClass SubClassOf NamedClass"),

    SUBCLASS_OF_OBJECT_SOME("NamedClass SubClassOf ObjectSomeValuesFrom"),

    SUBCLASS_OF_OBJECT_SOME_NOT("NamedClass SubClassOf ObjectSomeValuesFrom Not"),

    OBJECT_SOME_SUBCLASS_OF("ObjectSomeValuesFrom SubClassOf NamedClass"),

    SUBCLASS_OF_OBJECT_ALL("NamedClass SubClassOf ObjectAllValuesFrom"),

    SUBCLASS_OF_OBJECT_ALL_NOT("NamedClass SubClassOf ObjectAllValuesFrom Not"),

    SUBCLASS_OF_OBJECT_HAS_VALUE("NamedClass SubClassOf ObjectHasValue"),

    NAMED_CLASS_CLASS_ASSERTION("NamedClass ClassAssertion"),

    OBJECT_SOME_CLASS_ASSERTION("ObjectSomeValuesFrom ClassAssertion"),

    OBJECT_ALL_CLASS_ASSERTION("ObjectAllValuesFrom ClassAssertion"),

    SUBCLASS_OF_OBJECT_MIN_CARDINALITY("NamedClass SubClassOf ObjectMinCardinality"),

    SUBCLASS_OF_OBJECT_MAX_CARDINALITY("NamedClass SubClassOf ObjectMaxCardinality"),

    OBJECT_PROPERTY_ASSERTION("ObjectPropertyAssertion"),

    OBJECT_PROPERTY_DOMAIN("ObjectPropertyDomain"),

    OBJECT_PROPERTY_RANGE("ObjectPropertyRange"),

    OBJECT_ALL_SUBCLASS_OF("ObjectAllValuesFrom SubClassOf NamedClass"),

    SUB_OBJECT_PROPERTY_OF("SubObjectPropertyOf"),

    SUB_OBJECT_PROPERTY_OF_INVERSE("SubObjectPropertyOf ObjectInverseOf"),

    INVERSE_SUB_OBJECT_PROPERTY_OF("ObjectInverseOf SubPropertyOf"),

    DISJOINT_CLASSES("DisjointClasses"),
    
    LHS_CONJUNCTS("LHS Conjuncts");

    private String readableName;

    LemmaType(String name) {
        this.readableName = name;
    }


    /**
     * Returns the name of this enum constant, as contained in the
     * declaration.  This method may be overridden, though it typically
     * isn't necessary or desirable.  An enum type should override this
     * method when a more "programmer-friendly" string form exists.
     * @return the name of this enum constant
     */
    @Override
    public String toString() {
        return readableName;
    }
}

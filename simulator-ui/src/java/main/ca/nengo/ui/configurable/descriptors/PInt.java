/*
The contents of this file are subject to the Mozilla Public License Version 1.1
(the "License"); you may not use this file except in compliance with the License.
You may obtain a copy of the License at http://www.mozilla.org/MPL/

Software distributed under the License is distributed on an "AS IS" basis, WITHOUT
WARRANTY OF ANY KIND, either express or implied. See the License for the specific
language governing rights and limitations under the License.

The Original Code is "PInt.java". Description:
"Config Descriptor for Integers

  @author Shu Wu"

The Initial Developer of the Original Code is Bryan Tripp & Centre for Theoretical Neuroscience, University of Waterloo. Copyright (C) 2006-2008. All Rights Reserved.

Alternatively, the contents of this file may be used under the terms of the GNU
Public License license (the GPL License), in which case the provisions of GPL
License are applicable  instead of those above. If you wish to allow use of your
version of this file only under the terms of the GPL License and not to allow
others to use your version of this file under the MPL, indicate your decision
by deleting the provisions above and replace  them with the notice and other
provisions required by the GPL License.  If you do not delete the provisions above,
a recipient may use your version of this file under either the MPL or the GPL License.
 */

package ca.nengo.ui.configurable.descriptors;

import ca.nengo.ui.configurable.panels.IntegerPanel;

/**
 * Config Descriptor for Integers
 *
 * @author Shu Wu
 *
 */
public class PInt extends RangedConfigParam {

    private static final long serialVersionUID = 1L;

    /**
     * @param name TODO
     */
    public PInt(String name) {
        super(name);
    }
    
    public PInt(String name, String description) {
        super(name, description);
    }
    
    public PInt(String name, String description, int defaultValue) {
        super(name, description, defaultValue);
    }

    public PInt(String name, int defaultValue) {
        super(name, defaultValue);
    }

    /**
     * @param name TODO
     * @param defaultvalue TODO
     * @param min TODO
     * @param max TODO
     */
    public PInt(String name, int defaultvalue, int min, int max) {
        super(name, defaultvalue, min, max);
    }

    @Override
    protected IntegerPanel createInputPanel() {
        return new IntegerPanel(this);
    }

    @Override
    public Class<Integer> getTypeClass() {
        /*
         * Return the primitive type... Integer values can always be cast as the
         * primitive
         */
        return int.class;
    }

    @Override
    public String getTypeName() {
        return "Integer";
    }

}
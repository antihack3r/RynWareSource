// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.ui.settings.impl;

import java.util.function.Supplier;
import org.rynware.client.ui.settings.Setting;

public class NumberSetting extends Setting
{
    private final NumberType type;
    private float current;
    private float minimum;
    private float maximum;
    private float increment;
    private String desc;
    
    public NumberSetting(final String name, final float current, final float minimum, final float maximum, final float increment, final Supplier<Boolean> visible) {
        this.name = name;
        this.minimum = minimum;
        this.current = current;
        this.maximum = maximum;
        this.increment = increment;
        this.type = NumberType.DEFAULT;
        this.setVisible(visible);
    }
    
    public NumberSetting(final String name, final float current, final float minimum, final float maximum, final float increment, final Supplier<Boolean> visible, final NumberType type) {
        this.name = name;
        this.minimum = minimum;
        this.current = current;
        this.maximum = maximum;
        this.increment = increment;
        this.type = type;
        this.setVisible(visible);
    }
    
    public NumberSetting(final String name, final String desc, final float current, final float minimum, final float maximum, final float increment, final Supplier<Boolean> visible) {
        this.name = name;
        this.desc = desc;
        this.minimum = minimum;
        this.current = current;
        this.maximum = maximum;
        this.increment = increment;
        this.type = NumberType.DEFAULT;
        this.setVisible(visible);
    }
    
    public NumberSetting(final String name, final String desc, final float current, final float minimum, final float maximum, final float increment, final Supplier<Boolean> visible, final NumberType type) {
        this.name = name;
        this.desc = desc;
        this.minimum = minimum;
        this.current = current;
        this.maximum = maximum;
        this.increment = increment;
        this.type = type;
        this.setVisible(visible);
    }
    
    public String getDesc() {
        return this.desc;
    }
    
    public void setDesc(final String desc) {
        this.desc = desc;
    }
    
    public float getMinValue() {
        return this.minimum;
    }
    
    public void setMinValue(final float minimum) {
        this.minimum = minimum;
    }
    
    public float getMaxValue() {
        return this.maximum;
    }
    
    public void setMaxValue(final float maximum) {
        this.maximum = maximum;
    }
    
    public float getNumberValue() {
        return this.current;
    }
    
    public void setValueNumber(final float current) {
        this.current = current;
    }
    
    public float getIncrement() {
        return this.increment;
    }
    
    public void setIncrement(final float increment) {
        this.increment = increment;
    }
    
    public NumberType getType() {
        return this.type;
    }
    
    public enum NumberType
    {
        MS("Ms"), 
        APS("Aps"), 
        SIZE("Size"), 
        PERCENTAGE("Percentage"), 
        DISTANCE("Distance"), 
        DEFAULT("");
        
        String name;
        
        private NumberType(final String name) {
            this.name = name;
        }
        
        public String getName() {
            return this.name;
        }
    }
}

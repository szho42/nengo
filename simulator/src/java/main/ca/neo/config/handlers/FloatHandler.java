/*
 * Created on 17-Dec-07
 */
package ca.neo.config.handlers;

public class FloatHandler extends BaseHandler {

	public FloatHandler() {
		super(Float.class);
	}

	/**
	 * @see ca.neo.config.ConfigurationHandler#getDefaultValue(java.lang.Class)
	 */
	public Object getDefaultValue(Class c) {
		return new Float(0);
	}

}
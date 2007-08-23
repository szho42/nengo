package ca.neo.ui.configurable.targets.functions;

import ca.neo.math.impl.ConstantFunction;
import ca.neo.ui.configurable.struct.PTFloat;
import ca.neo.ui.configurable.struct.PTInt;
import ca.neo.ui.configurable.struct.PropDescriptor;

public class FConstantFunction extends ConfigurableFunction {
	static final PropDescriptor[] propStruct = new PropDescriptor[] {
			new PTInt("Dimension"), new PTFloat("Value") };

	@Override
	public PropDescriptor[] getConfigSchema() {
		return propStruct;
	}

	@Override
	public Class<ConstantFunction> getFunctionClass() {
		return ConstantFunction.class;
	}

	@Override
	public String getTypeName() {
		return "Constant Function";
	}

}

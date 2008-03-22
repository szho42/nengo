package ca.neo.ui.configurable.managers;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.text.MutableAttributeSet;

import ca.neo.ui.configurable.ConfigException;
import ca.neo.ui.configurable.Property;
import ca.neo.ui.configurable.PropertyInputPanel;
import ca.neo.ui.configurable.ConfigResult;
import ca.shu.ui.lib.actions.ActionException;
import ca.shu.ui.lib.objects.activities.TrackedAction;
import ca.shu.ui.lib.util.UserMessages;

/**
 * Configuration dialog
 * 
 * @author Shu Wu
 */
public class ConfigDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private JPanel myPanel;
	private JPanel myPropertyPanel;

	/**
	 * Parent ConfigurationManager
	 */
	private UserConfigurer myConfigManager;

	protected Vector<PropertyInputPanel> propertyInputPanels;

	/**
	 * @param configManager
	 *            Parent Configuration Manager
	 * @param owner
	 *            Component this dialog shall be added to
	 */
	public ConfigDialog(UserConfigurer configManager, Frame owner) {
		super(owner, configManager.getConfigurable().getTypeName());

		initialize(configManager, owner);

	}

	/**
	 * @param configManager
	 *            Parent Configuration Manager
	 * @param owner
	 *            Component this dialog shall be added to
	 */
	public ConfigDialog(UserConfigurer configManager, Dialog owner) {
		super(owner, "Configuring " + configManager.getConfigurable().getTypeName());

		initialize(configManager, owner);

	}

	/**
	 * @param setPropertyFields
	 *            if True, the user's values will be applied to the properties
	 *            set
	 * @return Whether the user has set all the values on the dialog correctly
	 */
	private boolean processPropertiesInternal(boolean setPropertyFields, boolean showMessage) {
		Iterator<PropertyInputPanel> it = propertyInputPanels.iterator();

		while (it.hasNext()) {
			PropertyInputPanel inputPanel = it.next();
			Property property = inputPanel.getDescriptor();

			if (inputPanel.isValueSet()) {
				if (setPropertyFields) {

					myConfigManager.setProperty(property.getName(), inputPanel.getValue());
				}
			} else {
				if (showMessage) {
					UserMessages.showWarning(property.getName() + " is not set or is incomplete");
				}
				pack();
				return false;
			}

		}
		pack();
		return true;
	}

	/**
	 * User wants to cancel the configuration
	 */
	private void cancelAction() {

		setVisible(false);

		myConfigManager.dialogConfigurationFinished(new ConfigDialogClosedException());
		super.dispose();
	}

	/**
	 * Creates ok, cancel buttons on the dialog
	 */
	private void createButtons(JPanel panel) {
		JPanel buttonsPanel = new VerticalLayoutPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
		buttonsPanel.add(Box.createHorizontalGlue());
		buttonsPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 5));

		JButton addToWorldButton = new JButton("Ok");
		addToWorldButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				okAction();
			}
		});
		buttonsPanel.add(addToWorldButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelAction();
			}
		});
		buttonsPanel.add(cancelButton);

		advancedButton = new JButton("Advanced");
		advancedButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				advancedAction();
			}
		});
		buttonsPanel.add(advancedButton);

		if (myConfigManager.getConfigurable().getSchema().getAdvancedProperties().size() == 0) {
			advancedButton.setVisible(false);
		}

		panel.add(buttonsPanel);
	}

	private JButton advancedButton;

	private boolean isAdvancedShown = false;

	private void advancedAction() {
		if (!isAdvancedShown) {
			isAdvancedShown = true;
			List<Property> advancedDescriptors = myConfigManager.getConfigurable().getSchema()
					.getAdvancedProperties();

			addDescriptors(advancedDescriptors);
		}
		// hide the button once it's been pressed
		advancedButton.setVisible(false);
	}

	private Component owner;

	/**
	 * Initialization to be called from the constructor
	 * 
	 * @param configManager
	 *            Configuration manager parent
	 * @param owner
	 *            Component the dialog is to be added to
	 */
	protected void initialize(UserConfigurer configManager, Component owner) {
		this.myConfigManager = configManager;
		this.owner = owner;

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent evt) {
				cancelAction();

			}
		});

		setResizable(false);
		setModal(true);

		myPanel = new VerticalLayoutPanel();
		myPanel.setVisible(true);
		myPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		initPanelTop(myPanel);

		myPropertyPanel = new VerticalLayoutPanel();
		myPanel.add(myPropertyPanel);

		addDescriptors(configManager.getConfigurable().getSchema().getProperties());

		initPanelBottom(myPanel);

		createButtons(myPanel);

		add(myPanel);

		setMinimumSize(new Dimension(200, this.getHeight()));
		updateBounds();

	}

	private void updateBounds() {
		pack();
		setLocationRelativeTo(owner);
	}

	protected void completeConfiguration() throws ConfigException {
		myConfigManager.getConfigurable().completeConfiguration(createConfigResult());
	}

	private ConfigResult createConfigResult() {
		return new ConfigResult(myConfigManager.getProperties());
	}

	/**
	 * What happens when the user presses the OK button
	 */
	private void okAction() {
		if (applyProperties()) {
			boolean preConfigurationSuccess = true;
			try {
				myConfigManager.getConfigurable().preConfiguration(createConfigResult());
			} catch (ConfigException e1) {
				e1.defaultHandleBehavior();
				preConfigurationSuccess = false;
			}

			if (preConfigurationSuccess) {
				setVisible(false);
				dispose();

				(new TrackedAction("Configuring " + myConfigManager.getConfigurable().getTypeName()) {

					private static final long serialVersionUID = 1L;

					@Override
					protected void action() throws ActionException {
						ConfigException configException = null;

						try {
							completeConfiguration();
						} catch (ConfigException e) {
							configException = e;

						}

						myConfigManager.dialogConfigurationFinished(configException);

					}
				}).doAction();
			}
		}
	}

	protected boolean checkPropreties() {
		return processPropertiesInternal(false, false);
	}

	/**
	 * Gets value entered in the dialog and applies them to the properties set
	 * 
	 * @return Whether operation was successful
	 */
	protected boolean applyProperties() {
		/*
		 * first check if all the fields have been set correctly, then set them
		 */
		if (processPropertiesInternal(false, true)) {
			processPropertiesInternal(true, false);
			return true;
		}
		return false;

	}

	/**
	 * Adds property descriptors to the panel
	 */
	protected void addDescriptors(List<Property> propDescriptors) {
		if (propertyInputPanels == null) {
			propertyInputPanels = new Vector<PropertyInputPanel>(propDescriptors.size());
		}

		MutableAttributeSet properties = myConfigManager.getProperties();

		for (Property property : propDescriptors) {

			PropertyInputPanel inputPanel = property.getInputPanel();
			myPropertyPanel.add(inputPanel.getJPanel());

			/*
			 * Try to get the configurer's current value and apply it to the
			 * input panels
			 */
			Object currentValue = properties.getAttribute(inputPanel.getName());
			if (currentValue != null) {
				inputPanel.setValue(currentValue);
			}

			propertyInputPanels.add(inputPanel);
		}

		checkPropreties();
		updateBounds();
	}

	/**
	 * Initializes the dialog contents top
	 */
	protected void initPanelTop(JPanel panel) {
		/*
		 * Used by subclasses to add elements to the panel
		 */
	}

	/**
	 * Initializes the dialog contents bottom
	 */
	protected void initPanelBottom(JPanel panel) {
		/*
		 * Used by subclasses to add elements to the panel
		 */
	}

	public UserConfigurer getConfigurer() {
		return myConfigManager;
	}

}

/**
 * Exception to be thrown if the Dialog is intentionally closed by the User
 * 
 * @author Shu
 */
class ConfigDialogClosedException extends ConfigException {

	private static final long serialVersionUID = 1L;

	public ConfigDialogClosedException() {
		super("Config dialog closed");

	}

	@Override
	public void defaultHandleBehavior() {
		/*
		 * Do nothing
		 */
	}

}
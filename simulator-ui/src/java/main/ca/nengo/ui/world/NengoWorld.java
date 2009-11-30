package ca.nengo.ui.world;

import java.awt.geom.Point2D;

import ca.nengo.model.Network;
import ca.nengo.model.Node;
import ca.nengo.ui.NengoGraphics;
import ca.nengo.ui.actions.CreateModelAction;
import ca.nengo.ui.models.NodeContainer;
import ca.nengo.ui.models.UINeoNode;
import ca.nengo.ui.models.constructors.CNetwork;
import ca.shu.ui.lib.actions.DragAction;
import ca.shu.ui.lib.util.menus.PopupMenuBuilder;
import ca.shu.ui.lib.world.WorldObject;
import ca.shu.ui.lib.world.elastic.ElasticWorld;

public class NengoWorld extends ElasticWorld implements NodeContainer {

	public NengoWorld() {
		super("Nengo");
	}

	@Override
	protected void constructMenu(PopupMenuBuilder menu) {

		super.constructMenu(menu);

		// Add models section
		menu.addSection("Add model");

		// Create network action
		menu.addAction(new CreateModelAction("New Network", this, new CNetwork()));
	}

	@Override
	public UINeoNode addNodeModel(Node node) throws ContainerException {
		return addNodeModel(node, null, null);
	}

	@Override
	public UINeoNode addNodeModel(Node node, Double posX, Double posY) throws ContainerException {
		if (!(node instanceof Network)) {
			throw new ContainerException("Only Networks are allowed to be added to the top-level Window");
		}

		UINeoNode nodeUI = UINeoNode.createNodeUI(node);

		if (posX != null && posY != null) {
			nodeUI.setOffset(posX, posY);

			getGround().addChild(nodeUI);
		} else {
			getGround().addChildFancy(nodeUI);
		}

		return nodeUI;
	}

	@Override
	public Node getNodeModel(String name) {
		for (WorldObject wo : getGround().getChildren()) {
			if (wo instanceof UINeoNode) {
				UINeoNode nodeUI = (UINeoNode) wo;

				if (nodeUI.getName().equals(name)) {
					return nodeUI.getModel();
				}
			}
		}
		return null;
	}

	@Override
	public Point2D localToView(Point2D localPoint) {
		localPoint = getSky().parentToLocal(localPoint);
		localPoint = getSky().localToView(localPoint);
		return localPoint;
	}

}

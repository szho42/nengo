package ca.shu.ui.lib.world.piccolo.primitives;

import java.net.URL;

import ca.shu.ui.lib.Style.Style;
import ca.shu.ui.lib.world.piccolo.WorldObjectImpl;

public class Image extends WorldObjectImpl {
	private PXImage imageNode;

	public Image(String fileName) {
		super(new PXImage(fileName));
		init();
	}

	public Image(URL url) {
		super(new PXImage(url));
		init();
	}

	public void init() {
		imageNode = (PXImage) getPiccolo();
		setPaint(Style.COLOR_BACKGROUND2);
		setPickable(false);
	}

	public boolean isLoadedSuccessfully() {

		if (imageNode.getImage() != null) {
			return true;
		} else {
			return false;
		}

	}

}

package net.sf.sveditor.core.tests;

import junit.framework.TestCase;
import net.sf.sveditor.core.SVCorePlugin;
import net.sf.sveditor.core.StringInputStream;
import net.sf.sveditor.core.db.ISVDBFileFactory;
import net.sf.sveditor.core.db.ISVDBScopeItem;
import net.sf.sveditor.core.db.SVDBFile;
import net.sf.sveditor.core.db.SVDBItem;
import net.sf.sveditor.core.db.SVDBItemType;
import net.sf.sveditor.core.db.SVDBMarkerItem;

public class SVDBTestUtils {

	public static void assertNoErrWarn(SVDBFile file) {
		for (SVDBItem it : file.getItems()) {
			if (it.getType() == SVDBItemType.Marker) {
				SVDBMarkerItem m = (SVDBMarkerItem)it;
				
				if (m.getName().equals(SVDBMarkerItem.MARKER_ERR) ||
						m.getName().equals(SVDBMarkerItem.MARKER_WARN)) {
					System.out.println("[ERROR] ERR/WARN: " + m.getMessage() +
							" @ " + file.getName() + ":" + m.getLocation().getLine());
					TestCase.fail("Unexpected " + m.getName() + " @ " + 
							file.getName() + ":" + m.getLocation().getLine());
				}
			}
		}
	}
	
	public static void assertFileHasElements(SVDBFile file, String ... elems) {
		for (String e : elems) {
			if (!findElement(file, e)) {
				TestCase.fail("Failed to find element \"" + e + "\" in file " + file.getName());
			}
		}
	}
	
	private static boolean findElement(ISVDBScopeItem scope, String e) {
		for (SVDBItem it : scope.getItems()) {
			if (it.getName().equals(e)) {
				return true;
			} else if (it instanceof ISVDBScopeItem) {
				if (findElement((ISVDBScopeItem)it, e)) {
					return true;
				}
			}
		}
		
		return false;
	}

	public static SVDBFile parse(String content, String filename) {
		SVDBFile file = null;
		ISVDBFileFactory factory = SVCorePlugin.createFileFactory(null);
		
		file = factory.parse(new StringInputStream(content), filename);
		
		return file;
	}

}
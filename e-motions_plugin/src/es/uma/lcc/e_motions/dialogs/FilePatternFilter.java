package es.uma.lcc.e_motions.dialogs;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class FilePatternFilter extends ViewerFilter {
	private String fileExtension;

	public FilePatternFilter(String ext) {
		super();
		fileExtension = ext;
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		IResource resource = null;
		if (element instanceof IResource) {
			resource = (IResource) element;
		} else if (element instanceof IAdaptable) {
			IAdaptable adaptable = (IAdaptable) element;
			resource = (IResource) adaptable.getAdapter(IResource.class);
		}
		if (resource != null && !resource.isDerived()) {
			if (resource.getType() != IResource.FILE
					|| resource.getFileExtension() == null 
					|| resource.getFileExtension().equals(fileExtension))
				return true;
		}
		return false;
	}

}

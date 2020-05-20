package org.xtext.example.mydsl.generator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.xtext.example.mydsl.md2.Greeting;
import org.xtext.example.mydsl.md2.Model;

import test.extension.IContentsGenerator;

public class Md2ContentsGenerator implements IContentsGenerator {

	@Override
	public String doGenerate(Resource resource) {
		StringBuilder sb = new StringBuilder(10);
		EObject obj = resource.getContents().get(0);
		if (obj instanceof Model) {
			Model model = (Model)obj;
			for(Greeting g :model.getGreetings()) {
				String hello = "Hello " + g.getName() + "!!\n";
				sb.append(hello);
			}
		}
		return sb.toString();
	}

}

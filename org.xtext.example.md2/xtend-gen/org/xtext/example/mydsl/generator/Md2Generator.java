/**
 * generated by Xtext 2.20.0
 */
package org.xtext.example.mydsl.generator;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.generator.AbstractGenerator;
import org.eclipse.xtext.generator.IFileSystemAccess2;
import org.eclipse.xtext.generator.IGeneratorContext;
import org.xtext.example.mydsl.generator.Md2ContentsGenerator;

/**
 * Generates code from your model files on save.
 * 
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#code-generation
 */
@SuppressWarnings("all")
public class Md2Generator extends AbstractGenerator {
  @Override
  public void doGenerate(final Resource resource, final IFileSystemAccess2 fsa, final IGeneratorContext context) {
    boolean _isEmpty = resource.getContents().isEmpty();
    boolean _not = (!_isEmpty);
    if (_not) {
      Md2ContentsGenerator gen = new Md2ContentsGenerator();
      String contents = gen.doGenerate(resource);
    }
  }
}
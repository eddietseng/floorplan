/*
 *  ====================================================================
 *  FeatureElementVisitor.java: Interface for visiting elements of that
 *  have implemented the feature interface ...
 * 
 *  Modified by: Mark Austin                                  June, 2012
 *  ====================================================================
 */

package model;

public interface FeatureElementVisitor {
    void visit( AbstractFeature         feature );
    void visit( AbstractCompoundFeature feature );
    void visit( CompositeHierarchy    composite );
    void start( CompositeHierarchy    composite );
    void finish( CompositeHierarchy   composite );
}


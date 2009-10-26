
package net.jgf.example.tanks.loader;



import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;

import net.jgf.config.Config;
import net.jgf.config.ConfigException;
import net.jgf.config.Configurable;
import net.jgf.config.ConfigurableFactory;
import net.jgf.entity.EntityGroup;
import net.jgf.jme.entity.SpatialEntity;
import net.jgf.jme.refs.SpatialReference;
import net.jgf.jme.scene.JmeScene;
import net.jgf.loader.LoadProperties;
import net.jgf.loader.Loader;
import net.jgf.loader.scene.SceneLoader;
import net.jgf.refs.HasReferences;
import net.jgf.refs.Reference;
import net.jgf.scene.Scene;
import net.jgf.system.Jgf;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;

import com.jme.scene.Node;
import com.jme.scene.Spatial;


/**
 */
@Configurable
public final class SceneReferencesProcessorLoader extends SceneLoader {

	/**
	 * Class logger
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(SceneReferencesProcessorLoader.class);

	public enum ReferenceProcessorType {
		model,
		entity,
		scene
	}

	public class ReferenceProcessor {

		public String regexp;

		public ReferenceProcessorType type;

		public Loader<Object> loader;

		public String target;

	}

	public ArrayList<ReferenceProcessor> processors = new ArrayList<ReferenceProcessor>();

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		int index = 1;
		String[] names;
		while (config.containsKey(configPath + "/reference[" + index + "]/@regexp")) {
			//names = config.getString(configPath + "/reference[" + index + "]/@name").split("\\s+");
			ReferenceProcessor proc = new ReferenceProcessor();
			proc.regexp = config.getString(configPath + "/reference[" + index + "]/@regexp");
			proc.type = ReferenceProcessorType.valueOf(config.getString(configPath + "/reference[" + index + "]/@type"));
			proc.target = config.getString(configPath + "/reference[" + index + "]/@target", null);
			proc.loader = ConfigurableFactory.newFromConfig(config, configPath + "/reference[" + index + "]/loader", Loader.class);
			processors.add(proc);
			index++;
		};

	}

	/**
	 * Loads a scene, including scene data
	 */
	@Override
	public Scene load(Scene base, LoadProperties properties) {

		combineProperties(properties);

		Collection<Reference> references = ((HasReferences)base).getReferences().getReferences();

		for (Reference ref : references) {
			processReference((JmeScene)base, ref, properties);
		}

		//ModelUtil.updateRenderStateRecursive(((JmeScene)base).getRootNode());

		return base;
	}

	protected void processReference(JmeScene scene, Reference ref, LoadProperties properties) {
		
		// Check if it matches any processor
		for (ReferenceProcessor proc : processors) {

			// Check if it matches
			
			boolean matches = Pattern.matches(proc.regexp, ref.getName());
			
			if (matches) {
				// Process it
				if (proc.type == ReferenceProcessorType.model) {

					Node anchor = (Node) ((SpatialReference) ref).getSpatial();

					// Create a subnode so trasformations are retained in the reference
					Node subnode = new Node(anchor.getName() + "-rtran");
					if (anchor.getChildren() != null) {
						ArrayList<Spatial> tmpChild = new ArrayList<Spatial>(anchor.getChildren().size());
						tmpChild.addAll(anchor.getChildren());
						anchor.detachAllChildren();
						for (Spatial spatial : tmpChild) {
							subnode.attachChild(spatial);
						}
					}

					// Create new properties as they should not be propagated / shared between loaders
					// TODO: Probably same strategy than chainloaders: copy prefs. for each subloader
					subnode = (Node) proc.loader.load(subnode, new LoadProperties());
					subnode.updateRenderState();
					anchor.attachChild(subnode);

					//((Node)(scene.getRootNode().getChild("fieldNode"))).attachChild(anchor);
					anchor.unlock();
					anchor.updateRenderState();
					anchor.updateWorldVectors(true);
					anchor.updateModelBound();
					anchor.updateGeometricState(0, true);
					anchor.lock();

				} else if (proc.type == ReferenceProcessorType.entity) {

					// TODO: Copy prefs! NO! In this one we need to override! Solve this!
					SpatialEntity entity = (SpatialEntity) proc.loader.load(null, new LoadProperties());
					Node refnode = ((Node) ((SpatialReference) ref).getSpatial());
					
					Node anchor = new Node(refnode.getName() + "-anchor");
					anchor.getLocalTranslation().set(refnode.getLocalTranslation());
					anchor.getLocalRotation().set(refnode.getLocalRotation());
					anchor.getLocalScale().set(refnode.getLocalScale());

					anchor.attachChild(entity.getSpatial());
					entity.setSpatial(anchor);
					scene.getRootNode().attachChild(anchor);

					anchor.unlock();
					anchor.updateRenderState();
					anchor.updateWorldVectors(true);
					anchor.updateModelBound();

					// TODO: This needs to be customizable?
					EntityGroup group = Jgf.getDirectory().getObjectAs("entity/root/enemy", EntityGroup.class);
					entity.setId(ref.getName());
					entity.integrate(group, scene.getRootNode());

				} else {
					throw new ConfigException("Unsupported reference processor of type '" + proc.type + "' when processing references at loader " + this.id);
				}
			}



		}
	}

}

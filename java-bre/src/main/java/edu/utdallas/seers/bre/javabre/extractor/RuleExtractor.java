package edu.utdallas.seers.bre.javabre.extractor;

import java.util.List;

import edu.utdallas.seers.bre.javabre.entity.BussinesRule;
import edu.utdallas.seers.bre.javabre.entity.JavaFileInfo;

public interface RuleExtractor {

	List<BussinesRule> extract(JavaFileInfo info);

}

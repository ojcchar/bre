package edu.utdallas.seers.bre.javabre.extractor;

import java.util.List;

import edu.utdallas.seers.bre.javabre.entity.BusinessRule;
import edu.utdallas.seers.bre.javabre.entity.JavaFileInfo;

public interface RuleExtractor {

	List<BusinessRule> extract(JavaFileInfo info);

}

package sentiment;

import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations.SentimentAnnotatedTree;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

public class nlpPipeline {

  static StanfordCoreNLP pipeline;

  public static void estimatingSentiment(final String text) {
    int sentimentInt;
    String sentimentName;
    final Annotation annotation = pipeline.process(text);
    for (final CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
      final Tree tree = sentence.get(SentimentAnnotatedTree.class);
      sentimentInt = RNNCoreAnnotations.getPredictedClass(tree);
      sentimentName = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
      System.out.println(sentimentName + "\t" + sentimentInt + "\t" + sentence);
    }
  }

  public static void init() {
    final Properties props = new Properties();
    props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
    pipeline = new StanfordCoreNLP(props);
  }
}

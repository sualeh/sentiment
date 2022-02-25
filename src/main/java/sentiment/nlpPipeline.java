package sentiment;

import java.util.List;
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

  public static void getReviewSentiment(final String review, final float weight) {
    int sentenceSentiment;
    int reviewSentimentAverageSum = 0;
    int reviewSentimentWeightedSum = 0;
    final Annotation annotation = pipeline.process(review);
    final List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
    final int numOfSentences = sentences.size();
    int factor = Math.round(numOfSentences * weight);
    if (factor == 0) {
      factor = 1;
    }
    final int divisorLinear = numOfSentences;
    int divisorWeighted = 0;

    for (int i = 0; i < numOfSentences; i++) {
      final Tree tree = sentences.get(i).get(SentimentAnnotatedTree.class);
      sentenceSentiment = RNNCoreAnnotations.getPredictedClass(tree);
      reviewSentimentAverageSum = reviewSentimentAverageSum + sentenceSentiment;
      if (i == 0 || i == numOfSentences - 1) {
        reviewSentimentWeightedSum = reviewSentimentWeightedSum + sentenceSentiment * factor;
        divisorWeighted += factor;
      } else {
        reviewSentimentWeightedSum = reviewSentimentWeightedSum + sentenceSentiment;
        divisorWeighted += 1;
      }
    }
    System.out.println("Number of sentences:\t\t" + numOfSentences);
    System.out.println("Adapted weighting factor:\t" + factor);
    System.out.println(
        "Weighted average sentiment:\t"
            + Math.round((float) reviewSentimentWeightedSum / divisorWeighted));
    System.out.println(
        "Linear average sentiment:\t"
            + Math.round((float) reviewSentimentAverageSum / divisorLinear));
  }

  public static void init() {
    final Properties props = new Properties();
    props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
    pipeline = new StanfordCoreNLP(props);
  }
}

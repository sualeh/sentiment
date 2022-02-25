package sentiment;

public class OverallReviewSentiment {
  public static void main(final String[] args) {
    final String text =
        "I would recommend this book for anyone who wants an introduction to natural language processing. Just finished the book and followed the code all way. I tried the code from the resource web site. I like how it is organized. Well done.";
    nlpPipeline.init();
    nlpPipeline.getReviewSentiment(text, 0.4f);
  }
}

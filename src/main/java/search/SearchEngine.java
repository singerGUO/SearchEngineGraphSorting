package search;

import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
import misc.Searcher;
import search.analyzers.PageRankAnalyzer;
import search.analyzers.TfIdfAnalyzer;
import search.misc.exceptions.DataExtractionException;
import search.misc.Bridge;
import search.models.Result;
import search.models.Webpage;
import search.models.WebpageSummary;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * A class that bundles together all of our search engine functionality into
 * a single class.
 */
public class SearchEngine {
    /**
     * Some constants to tune PageRankAnalyzer. You do not need to
     * modify these constants.
     */
    public static double PAGE_RANK_DECAY = 0.85;
    public static double PAGE_RANK_EPSILON = 0.0001;
    public static int PAGE_RANK_ITERATION_LIMIT = 200;

    private ISet<WebpageSummary> pages;
    private TfIdfAnalyzer tfIdfAnalyzer;
    private PageRankAnalyzer pageRankAnalyzer;

    /**
     * Creates a new instance of this class set up to search and return results
     * using the webpages located at the given provided folder.
     */
    public SearchEngine(String dataFolderName) {
        ISet<Webpage> webpages = this.collectWebpages(Paths.get("data", dataFolderName));
        this.pages = this.extractWebpageSummaries(webpages);

        this.tfIdfAnalyzer = new TfIdfAnalyzer(webpages);
        this.pageRankAnalyzer = new PageRankAnalyzer(
                webpages,
                PAGE_RANK_DECAY,
                PAGE_RANK_EPSILON,
                PAGE_RANK_ITERATION_LIMIT);
    }

    /**
     * Returns a numeric rank representing the relevance of the given query to the URI.
     *
     * The returned number will never be negative.
     */
    public double computeScore(IList<String> query, URI uri) {
        double tfIdf = this.tfIdfAnalyzer.computeRelevance(query, uri);
        double pageRank = this.pageRankAnalyzer.computePageRank(uri);

        if (pageRank <= 0.0) {
            throw new IllegalStateException(String.format(
                    "Page '%s' had a page rank of '%f'; all page ranks should be positive and non-zero.",
                    uri, pageRank));
        }

        // We are combining these two scores in a fairly arbitrary way.
        // The correct thing to do is to apply machine learning and develop
        // a classifier that combines these two scores.
        //
        // Figuring out the best way to do this is something of a black art
        // and is a part of the "secret sauce" of commerical web engines.
        //
        // However, in the interests of simplicity, we'll just use a
        // weighted average. This is a pretty arbitrary choice: feel
        // free to modify this method however you want!
        //
        // We will be grading your TfIdfAnalyzer and PageRankAnalyzer
        // classes separately, but not this method.
        return tfIdf * 0.7 + pageRank * 0.3;
    }

    /**
     * Returns the top k webpages that match this query.
     *
     * The results are returned in *display order*: the best results come
     * first, and the worst results come last.
     */
    public IList<Result> getTopKResults(IList<String> query, int k) {
        IList<Result> results = new DoubleLinkedList<>();

        for (WebpageSummary summary: this.pages) {
            double score = this.computeScore(query, summary.getUri());
            results.add(new Result(summary, score));
        }

        IList<Result> topK = Searcher.topKSort(k, results);

        IList<Result> reversed = new DoubleLinkedList<>();
        for (Result res : topK) {
            reversed.insert(0, res);
        }

        return reversed;
    }

    private ISet<Webpage> collectWebpages(Path root) {
        try {
            return Files.walk(root)
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".htm") || path.toString().endsWith(".html"))
                    .map(Path::toUri)
                    .map(Webpage::load)
                    .collect(Bridge.toISet());
        } catch (IOException ex) {
            throw new DataExtractionException("Could not find given root folder", ex);
        }
    }

    private ISet<WebpageSummary> extractWebpageSummaries(ISet<Webpage> pages) {
        ISet<WebpageSummary> output = new ChainedHashSet<>();
        for (Webpage page : pages) {
            output.add(page.getSummary());
        }
        return output;
    }
}

package search.analyzers;

import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
//import misc.exceptions.NotYetImplementedException;
import search.models.Webpage;

//import javax.print.attribute.HashDocAttributeSet;
import java.net.URI;

/**
 * This class is responsible for computing how "relevant" any given document is
 * to a given search query.
 *
 * See the spec for more details.
 */
public class TfIdfAnalyzer {
    // This field must contain the IDF score for every single word in all
    // the documents.
    private IDictionary<String, Double> idfScores;

    // This field must contain the TF-IDF vector for each webpage you were given
    // in the constructor.
    //
    // We will use each webpage's page URI as a unique key.
    private IDictionary<URI, IDictionary<String, Double>> documentTfIdfVectors;

    // Feel free to add extra fields and helper methods.
    //each document's norm vector
    private IDictionary<URI, Double> normVector;

    public TfIdfAnalyzer(ISet<Webpage> webpages) {
        // Implementation note: We have commented these method calls out so your
        // search engine doesn't immediately crash when you try running it for the
        // first time.
        //
        // You should uncomment these lines when you're ready to begin working
        // on this class.
        // documentTfIdfVectors=new ChainedHashDictionary<>();
        this.idfScores = this.computeIdfScores(webpages);
        this.documentTfIdfVectors = this.computeAllDocumentTfIdfVectors(webpages);
        normVector = new ChainedHashDictionary<>();

        for (KVPair<URI, IDictionary<String, Double>> page : documentTfIdfVectors) {
            normVector.put(page.getKey(), norm(page.getValue()));
        }
    }

    // Note: this method, strictly speaking, doesn't need to exist. However,
    // we've included it so we can add some unit tests to help verify that your
    // constructor correctly initializes your fields.
    public IDictionary<URI, IDictionary<String, Double>> getDocumentTfIdfVectors() {

        return this.documentTfIdfVectors;
    }

    // Note: these private methods are suggestions or hints on how to structure your
    // code. However, since they're private, you're not obligated to implement exactly
    // these methods: feel free to change or modify these methods however you want. The
    // important thing is that your 'computeRelevance' method ultimately returns the
    // correct answer in an efficient manner.

    /**
     * Return a dictionary mapping every single unique word found
     * in every single document to their IDF score.
     */
    //total ln(number of documents/numberofdocumentscontaining term)
    private IDictionary<String, Double> computeIdfScores(ISet<Webpage> pages) {

        IDictionary<String, Double> scoreMap = new ChainedHashDictionary<>();

        for (Webpage page : pages) {
            ISet<String> uniqueSet = new ChainedHashSet<>();
            IList<String> nameList = page.getWords();
            for (String name : nameList) {
                //all of pages have only count 1 because for the idf we only care about how many pages have
                //key words
                uniqueSet.add(name);
            }


            for (String uniqueName : uniqueSet) {
                scoreMap.put(uniqueName, scoreMap.getOrDefault(uniqueName, 0.0) + 1);
            }
        }
        IDictionary<String, Double> idfScore = new ChainedHashDictionary<>();
        for (KVPair<String, Double> entry : scoreMap) {
            double score = Math.log(pages.size() / entry.getValue());
            idfScore.put(entry.getKey(), score);
        }

        return idfScore;

    }
    /**
     * Returns a dictionary mapping every unique word found in the given list
     * to their term frequency (TF) score.
     *
     * The input list represents the words contained within a single document.
     */
    private IDictionary<String, Double> computeTfScores(IList<String> words) {
        IDictionary<String, Double> counter = new ChainedHashDictionary<>();
        for (String s : words) {
            counter.put(s, counter.getOrDefault(s, 0.0) + 1);
        }
        IDictionary<String, Double> result = new ChainedHashDictionary<>();

        for (KVPair<String, Double> pair : counter) {
            result.put(pair.getKey(), pair.getValue() / words.size());
        }
        return result;



    }

    /**
     * See spec for more details on what this method should do.
     */
    private IDictionary<URI, IDictionary<String, Double>> computeAllDocumentTfIdfVectors(ISet<Webpage> pages) {
        // Hint: this method should use the idfScores field and
        // call the computeTfScores(...) method.
        IDictionary<URI, IDictionary<String, Double>> vector = new ChainedHashDictionary<>();
        IDictionary<String, Double> tfScore;
        for (Webpage w : pages) {
            tfScore = computeTfScores(w.getWords());
            for (KVPair<String, Double> idf : idfScores) {

                if (tfScore.containsKey(idf.getKey())) {
                    double score = tfScore.get(idf.getKey()) * idf.getValue();
                    tfScore.put(idf.getKey(), score);
                    vector.put(w.getUri(), tfScore);
                }
            }
        }

        return vector;
    }

    /**
     * Returns the cosine similarity between the TF-IDF vector for the given query and the
     * URI's document.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     *               webpages given to the constructor.
     */
    public Double computeRelevance(IList<String> query, URI pageUri) {
        // Note: The pseudocode we gave you is not very efficient. When implementing,
        // this method, you should:
        //
        // 1. Figure out what information can be precomputed in your constructor.
        //    Add a third field containing that information.
        //
        // 2. See if you can combine or merge one or more loops.
        //one of the document data
        //in this method we compute the relevance between name and one document
        IDictionary<String, Double> documentVector = documentTfIdfVectors.get(pageUri);
        IDictionary<String, Double> queryVector = new ChainedHashDictionary<>();
        IDictionary<String, Double> queryTf = this.computeTfScores(query);
        double numerator = 0.0;
        double denominator = 0.0;
        for (KVPair<String, Double> pair : queryTf) {
            //tfidf vector
            //we might have some new name
            if (idfScores.containsKey(pair.getKey())) {
                queryVector.put(pair.getKey(), pair.getValue() * idfScores.get(pair.getKey()));
            }

        }
        //calculate query unqiue word for one iteration ,norm of query vector is the sum of score*score
        // compute numerator
        for (KVPair<String, Double> word : queryVector) {
            // the query might have something new.
            double docWordScore = 0.0;
            if (documentVector.containsKey(word.getKey())) {
                docWordScore = documentVector.get(word.getKey());
            }
            double queryWordScore = queryVector.get(word.getKey());
            numerator += queryWordScore * docWordScore;

        }
        denominator = normVector.get(pageUri) * norm(queryVector);
        if (denominator != 0) {
            return numerator / denominator;
        }
        return 0.0;
    }

    private double norm(IDictionary<String, Double> vector) {
        double output = 0.0;
        for (KVPair<String, Double> s : vector) {
            double score = s.getValue();
            output += score * score;
        }
        return Math.sqrt(output);
    }
}

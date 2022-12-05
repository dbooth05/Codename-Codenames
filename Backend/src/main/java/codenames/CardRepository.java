package codenames;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Benjamin Kelly
 *
 */

public interface CardRepository extends JpaRepository<Card, Long> {
	
	/**
	 * gets a word by id
	 * @param id
	 * @return a word object
	 */
    Card findById(int id);

    /**
     * deletes a word by id
     * @param id
     */
    @Transactional
    void deleteById(int id);
    
    /**
     * finds the object by given word associated
     * @param word
     * @return card object
     */
    Card findByword(String word);
}
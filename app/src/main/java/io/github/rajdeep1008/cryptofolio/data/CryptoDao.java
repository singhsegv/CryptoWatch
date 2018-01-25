package io.github.rajdeep1008.cryptofolio.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by rajdeep1008 on 23/01/18.
 */

@Dao
public interface CryptoDao {

    @Insert
    void insertAll(List<Crypto> cryptos);

    @Update
    void updateAll(List<Crypto> cryptos);

    @Query("SELECT * FROM crypto")
    List<Crypto> getAll();

    @Query("SELECT * FROM crypto WHERE id IN (:ids)")
    List<Crypto> loadFavorites(List<String> ids);

    @Query("SELECT * FROM crypto WHERE id LIKE :cryptoId")
    Crypto getById(String cryptoId);

    @Query("SELECT * FROM crypto WHERE symbol LIKE :idQuery or name LIKE :nameQuery")
    List<Crypto> searchCryptos(String idQuery, String nameQuery);

    @Query("SELECT COUNT(*) from crypto")
    int getCryptoCount();

    @Query("DELETE FROM crypto")
    void clearTable();
}

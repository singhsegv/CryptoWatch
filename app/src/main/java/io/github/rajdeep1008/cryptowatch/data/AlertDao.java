package io.github.rajdeep1008.cryptowatch.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by rajdeep1008 on 05/02/18.
 */

@Dao
public interface AlertDao {

    @Insert
    void insertAll(List<AlertCrypto> items);

    @Insert
    void insertSingle(AlertCrypto item);

    @Update
    void updateAll(List<AlertCrypto> items);

    @Query("SELECT * FROM alert_crypto")
    List<AlertCrypto> getAll();

    @Query("SELECT * FROM alert_crypto WHERE id LIKE :cryptoId")
    AlertCrypto getById(String cryptoId);

    @Query("SELECT COUNT(*) from alert_crypto")
    int getCryptoCount();

    @Query("DELETE FROM alert_crypto")
    void clearTable();

    @Query("DELETE FROM alert_crypto WHERE id LIKE :cryptoId")
    void removeFromList(String cryptoId);
}

package com.goblin.qrhunter.data;

import com.goblin.qrhunter.QRCode;

public class QRCodeRepository extends FirestoreRepository<QRCode> {
    protected QRCodeRepository(String collectionPath) {
        super(collectionPath, QRCode.class);
    }

    public QRCodeRepository() {
        super("/codes", QRCode.class);
    }
}

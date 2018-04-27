package com.github.ontio.sdk.manager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.IOUtils;
import com.github.ontio.OntSdk;
import com.github.ontio.common.Common;
import com.github.ontio.sdk.wallet.Identity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.InputStream;

import static org.junit.Assert.*;

public class RecordTxTest {

    OntSdk ontSdk;
    Identity id;
    String codeAddress;

    @Before
    public void setUp() throws Exception {

        ontSdk = OntSdk.getInstance();
        String restUrl = "http://127.0.0.1:20334";
        String codeHex = "013dc56b6c766b00527ac46c766b51527ac46161682d53797374656d2e457865637574696f6e456e67696e652e476574457865637574696e67536372697074486173686c766b52527ac400756c766b00c3125265674964576974685075626c69634b6579876c766b53527ac46c766b53c3645d00616c766b51c3c0529c009c6c766b56527ac46c766b56c3640e00006c766b57527ac462ca066c766b51c300c36c766b54527ac46c766b51c351c36c766b55527ac46c766b54c36c766b55c3617c65a8066c766b57527ac46295066c766b00c31352656749645769746841747472696275746573876c766b58527ac46c766b58c3647100616c766b51c3c0539c009c6c766b5c527ac46c766b5cc3640e00006c766b57527ac46247066c766b51c300c36c766b59527ac46c766b51c351c36c766b5a527ac46c766b51c352c36c766b5b527ac46c766b59c36c766b5ac36c766b5bc36152726565076c766b57527ac462fe056c766b00c3064164644b6579876c766b5d527ac46c766b5dc3647300616c766b51c3c0539c009c6c766b0111527ac46c766b0111c3640e00006c766b57527ac462bb056c766b51c300c36c766b5e527ac46c766b51c351c36c766b5f527ac46c766b51c352c36c766b60527ac46c766b5ec36c766b5fc36c766b60c361527265580b6c766b57527ac46272056c766b00c30952656d6f76654b6579876c766b0112527ac46c766b0112c3647900616c766b51c3c0539c009c6c766b0116527ac46c766b0116c3640e00006c766b57527ac4622a056c766b51c300c36c766b0113527ac46c766b51c351c36c766b0114527ac46c766b51c352c36c766b0115527ac46c766b0113c36c766b0114c36c766b0115c361527265480c6c766b57527ac462db046c766b00c30c416464417474726962757465876c766b0117527ac46c766b0117c364b500616c766b51c3c0559c009c6c766b011d527ac46c766b011dc3640e00006c766b57527ac46290046c766b51c300c36c766b0118527ac46c766b51c351c36c766b0119527ac46c766b51c352c36c766b011a527ac46c766b51c353c36c766b011b527ac46c766b51c354c36c766b011c527ac46c766b0118c36c766b0119c36c766b011ac36c766b011bc36c766b011cc361547951795672755172755379527955727552727565b20e6c766b57527ac46205046c766b00c30f52656d6f7665417474726962757465876c766b011e527ac46c766b011ec3647900616c766b51c3c0539c009c6c766b0122527ac46c766b0122c3640e00006c766b57527ac462b7036c766b51c300c36c766b011f527ac46c766b51c351c36c766b0120527ac46c766b51c352c36c766b0121527ac46c766b011fc36c766b0120c36c766b0121c361527265ee0f6c766b57527ac46268036c766b00c30b4164645265636f76657279876c766b0123527ac46c766b0123c3647900616c766b51c3c0539c009c6c766b0127527ac46c766b0127c3640e00006c766b57527ac4621e036c766b51c300c36c766b0124527ac46c766b51c351c36c766b0125527ac46c766b51c352c36c766b0126527ac46c766b0124c36c766b0125c36c766b0126c361527265ac0b6c766b57527ac462cf026c766b00c30e4368616e67655265636f76657279876c766b0128527ac46c766b0128c3647900616c766b51c3c0539c009c6c766b012c527ac46c766b012cc3640e00006c766b57527ac46282026c766b51c300c36c766b0129527ac46c766b51c351c36c766b012a527ac46c766b51c352c36c766b012b527ac46c766b0129c36c766b012ac36c766b012bc361527265090c6c766b57527ac46233026c766b00c3114164644174747269627574654172726179876c766b012d527ac46c766b012dc364050061616c766b00c30e4765745075626c69634b65794964876c766b012e527ac46c766b012ec3643d00616c766b51c300c36c766b012f527ac46c766b51c351c36c766b0130527ac46c766b012fc36c766b0130c3617c65d8136c766b57527ac462a8016c766b00c3124765745075626c69634b6579537461747573876c766b0131527ac46c766b0131c3643d00616c766b51c300c36c766b0132527ac46c766b51c351c36c766b0133527ac46c766b0132c36c766b0133c3617c65e2136c766b57527ac46244016c766b00c30d4765745075626c69634b657973876c766b0134527ac46c766b0134c3644d00616c766b51c3c0519c009c6c766b0136527ac46c766b0136c3640e00006c766b57527ac462f8006c766b51c300c36c766b0135527ac46c766b0135c36165030f6c766b57527ac462d5006c766b00c30d47657441747472696275746573876c766b0137527ac46c766b0137c3644d00616c766b51c3c0519c009c6c766b0139527ac46c766b0139c3640e00006c766b57527ac46289006c766b51c300c36c766b0138527ac46c766b0138c361659c106c766b57527ac46266006c766b00c30647657444444f876c766b013a527ac46c766b013ac3643d00616c766b51c300c36c766b013b527ac46c766b51c351c36c766b013c527ac46c766b013bc36c766b013cc3617c657b136c766b57527ac4620e00006c766b57527ac46203006c766b57c3616c756658c56b6c766b00527ac46c766b51527ac4616c766b00c361658a166c766b52527ac46c766b52c3c0519f630f006c766b52c36165d113620400516c766b53527ac46c766b53c3640e00006c766b54527ac462fa006c766b51c36168184e656f2e52756e74696d652e436865636b5769746e657373009c6c766b55527ac46c766b55c3640e00006c766b54527ac462be006c766b52c36c766b51c3617c65ce166c766b56527ac46c766b56c364950061516c766b57527ac46c766b52c36165bf13616c766b52c36c766b57c36c766b51c3615272650317616c766b52c36c766b51c36c766b57c361527265ac17616c766b52c300617c651b14616c766b52c351617c65b014610872656769737465726c766b00c3617c08526567697374657253c168124e656f2e52756e74696d652e4e6f7469667961516c766b54527ac4620e00006c766b54527ac46203006c766b54c3616c7566011fc56b6c766b00527ac46c766b51527ac46c766b52527ac4616c766b00c361652e156c766b53527ac46c766b53c3c0519f630f006c766b53c361657512620400516c766b5e527ac46c766b5ec3640e00006c766b5f527ac4621d046c766b51c36168184e656f2e52756e74696d652e436865636b5769746e657373009c6c766b60527ac46c766b60c3640e00006c766b5f527ac462e1036c766b52c3c0640e006c766b52c300517f620400006c766b5a527ac46c766b52c3c051946c766b5b527ac4020001c56c766b5c527ac4006c766b54527ac4516c766b55527ac4006c766b56527ac462cf02616c766b5bc3529f6c766b0116527ac46c766b0116c3641100516c766b56c3966c766b56527ac46c766b52c36c766b55c3517f020001956c766b52c36c766b55c35193517f936c766b0111527ac46c766b5bc36c766b0111c352939f6c766b0117527ac46c766b0117c3641100516c766b56c3966c766b56527ac46c766b52c36c766b55c352936c766b0111c37f6c766b0112527ac46c766b0112c3c051a0009c6c766b0118527ac46c766b0118c3641100516c766b56c3966c766b56527ac46c766b0112c300517f6c766b57527ac46c766b0112c3c0516c766b57c393a0009c6c766b0119527ac46c766b0119c3641100516c766b56c3966c766b56527ac46c766b0112c3516c766b57c393517f6c766b58527ac46c766b0112c3c0546c766b57c3936c766b58c3939f6c766b011a527ac46c766b011ac3641100516c766b56c3966c766b56527ac46c766b0112c3526c766b57c3936c766b58c393517f020001956c766b0112c3536c766b57c3936c766b58c393517f936c766b59527ac46c766b57c36c766b58c3936c766b59c39354936c766b0111c39c009c6c766b011b527ac46c766b011bc3641100516c766b56c3966c766b56527ac46c766b0112c3516c766b57c37f6c766b0113527ac46c766b0112c3526c766b57c3936c766b58c37f6c766b0114527ac46c766b0112c3546c766b57c3936c766b58c3936c766b59c37f6c766b0115527ac46c766b58c302ff00a06c766b011c527ac46c766b011cc3640e00006c766b5f527ac4625f016c766b53c36c766b0113c3617c6577146c766b011d527ac46c766b011dc36433006c766b53c36c766b0113c36c766b0114c36c766b0115c3615379517955727551727552795279547275527275650519616c766b5cc36c766b54c36c766b0113c3c46c766b55c352936c766b0111c3936c766b55527ac46c766b5bc3526c766b0111c393946c766b5b527ac4616c766b54c351936c766b54527ac46c766b54c36c766b5ac39f6c766b011e527ac46c766b011ec3631bfd516c766b5d527ac46c766b53c36165490f616c766b53c36c766b51c3617c652712756c766b53c351617c656210616c766b53c36c766b54c3617c65b10f616c766b53c36c766b5dc36c766b51c3615272656112616c766b53c36c766b51c36c766b5dc3615272650a13610872656769737465726c766b00c3617c08526567697374657253c168124e656f2e52756e74696d652e4e6f7469667961516c766b5f527ac46203006c766b5fc3616c756659c56b6c766b00527ac46c766b51527ac46c766b52527ac4616c766b00c36165b0106c766b53527ac46c766b53c3c0519f6311006c766b53c36165f70d009c620400516c766b55527ac46c766b55c3640e00006c766b56527ac46224016c766b52c36168184e656f2e52756e74696d652e436865636b5769746e657373642d006c766b53c36c766b52c3617c6568076317006c766b00c36c766b52c3617c655b0d009c62040000620400516c766b57527ac46c766b57c3640e00006c766b56527ac462bc006c766b53c36165c20e6c766b54527ac46c766b53c36c766b51c3617c65b6106c766b58527ac46c766b58c3648300616c766b53c36c766b54c35193617c65dc0e616c766b53c36c766b51c36c766b54c3519361527265a811616c766b53c36c766b54c351936c766b51c361527265d11061036164646c766b00c36c766b51c3615272095075626c69634b657954c168124e656f2e52756e74696d652e4e6f7469667961516c766b56527ac4620e00006c766b56527ac46203006c766b56c3616c75665ac56b6c766b00527ac46c766b51527ac46c766b52527ac4616c766b00c36165290f6c766b53527ac46c766b53c3c0519f6311006c766b53c36165700c009c620400516c766b55527ac46c766b55c3640e00006c766b56527ac4620d016c766b52c36168184e656f2e52756e74696d652e436865636b5769746e6573736417006c766b53c36c766b52c3617c65e105009c620400516c766b57527ac46c766b57c3640e00006c766b56527ac462bb006c766b53c36165510d6c766b54527ac46c766b53c36c766b51c3617c657b0f6c766b58527ac46c766b58c3648200616c766b00c36c766b51c3617c65d9096c766b59527ac46c766b53c36c766b54c35194617c65550d616c766b53c36c766b59c36c766b51c361527265c30f610672656d6f76656c766b00c36c766b51c3615272095075626c69634b657954c168124e656f2e52756e74696d652e4e6f7469667961516c766b56527ac4620e00006c766b56527ac46203006c766b56c3616c756658c56b6c766b00527ac46c766b51527ac46c766b52527ac4616c766b00c36165b90d6c766b53527ac46c766b53c3c0519f6311006c766b53c36165000b009c620400516c766b54527ac46c766b54c3640e00006c766b55527ac46296006c766b52c36168184e656f2e52756e74696d652e436865636b5769746e6573736417006c766b53c36c766b52c3617c657104009c620400516c766b56527ac46c766b56c3640e00006c766b55527ac46244006c766b53c36165820cc000a06c766b57527ac46c766b57c3640e00006c766b55527ac4621e006c766b53c36c766b51c3617c65a70c61516c766b55527ac46203006c766b55c3616c756657c56b6c766b00527ac46c766b51527ac46c766b52527ac4616c766b00c36165c00c6c766b53527ac46c766b53c3c0519f6311006c766b53c36165070a009c620400516c766b54527ac46c766b54c3640e00006c766b55527ac46274006c766b52c36c766b53c36165d60b617c6558146428006c766b52c36168184e656f2e52756e74696d652e436865636b5769746e657373009c620400516c766b56527ac46c766b56c3640e00006c766b55527ac4621e006c766b53c36c766b51c3617c65d00b61516c766b55527ac46203006c766b55c3616c75665bc56b6c766b00527ac46c766b51527ac46c766b52527ac46c766b53527ac46c766b54527ac4616c766b00c36165db0b6c766b55527ac46c766b55c3c0519f6311006c766b55c361652209009c620400516c766b56527ac46c766b56c3640e00006c766b57527ac46268016c766b54c36168184e656f2e52756e74696d652e436865636b5769746e6573736417006c766b55c36c766b54c3617c659302009c620400516c766b58527ac46c766b58c3640e00006c766b57527ac46216016c766b55c36c766b51c3617c658f0d6c766b59527ac46c766b59c3648900616c766b55c3616543096c766b5a527ac46c766b55c36c766b5ac35193617c657c09616c766b55c36c766b51c36c766b52c36c766b53c361537951795572755172755279527954727552727565ff1161036164646c766b00c36c766b51c36152720941747472696275746554c168124e656f2e52756e74696d652e4e6f746966796161626700616c766b55c36c766b51c36c766b52c36c766b53c3615379517955727551727552795279547275527275659b1161067570646174656c766b00c36c766b51c36152720941747472696275746554c168124e656f2e52756e74696d652e4e6f746966796161516c766b57527ac46203006c766b57c3616c756659c56b6c766b00527ac46c766b51527ac46c766b52527ac4616c766b00c36165100a6c766b53527ac46c766b53c3c0519f6311006c766b53c361655707009c620400516c766b54527ac46c766b54c3640e00006c766b55527ac462f1006c766b52c36168184e656f2e52756e74696d652e436865636b5769746e6573736417006c766b53c36c766b52c3617c65c800009c620400516c766b56527ac46c766b56c3640e00006c766b55527ac4629f006c766b53c36c766b51c3617c65fa0b6c766b57527ac46c766b57c3647600616c766b53c3616578076c766b58527ac46c766b53c36c766b58c35194617c65b107616c766b53c36c766b51c3617c65c610610672656d6f76656c766b53c36c766b51c36152720941747472696275746554c168124e656f2e52756e74696d652e4e6f7469667961516c766b55527ac4620e00006c766b55527ac46203006c766b55c3616c756654c56b6c766b00527ac46c766b51527ac4616c766b00c3527e6c766b51c3617c65e80cc0009c6c766b52527ac46c766b52c3640e00006c766b53527ac4620e00516c766b53527ac46203006c766b53c3616c75665ec56b6c766b00527ac4616c766b00c3616576086c766b51527ac400006c766b53527ac46168164e656f2e53746f726167652e476574436f6e746578746c766b51c3527e617c680f4e656f2e53746f726167652e4765746c766b54527ac46c766b54c3c0009c6c766b59527ac46c766b59c3640e00006c766b5a527ac46282016c766b54c36c766b55527ac46168164e656f2e53746f726167652e476574436f6e746578746c766b51c3587e6c766b55c37e617c680f4e656f2e53746f726167652e4765746c766b56527ac46c766b56c361659b0d6c766b55c36165920d7e6c766b52527ac462dd00616168164e656f2e53746f726167652e476574436f6e746578746c766b51c3527e6c766b55c37e617c680f4e656f2e53746f726167652e4765746c766b5b527ac46c766b5bc36165b00a6c766b55527ac46c766b53c351936c766b53527ac46c766b55c3c0009c6c766b5c527ac46c766b5cc3640600626f006168164e656f2e53746f726167652e476574436f6e746578746c766b51c3587e6c766b55c37e617c680f4e656f2e53746f726167652e4765746c766b56527ac46c766b52c36c766b56c36165c00c6c766b55c36165b70c7e7e6c766b52527ac461516c766b5d527ac4621eff6c766b53c36165c60b6c766b57527ac46c766b57c36c766b52c37e6c766b58527ac46c766b58c36c766b5a527ac46203006c766b5ac3616c75665cc56b6c766b00527ac4616c766b00c361656e066c766b51527ac400006c766b53527ac46168164e656f2e53746f726167652e476574436f6e746578746c766b51c3557e617c680f4e656f2e53746f726167652e4765746c766b54527ac46c766b54c3c0009c6c766b55527ac46c766b55c3640f0061006c766b56527ac4627301616c766b54c36c766b57527ac46168164e656f2e53746f726167652e476574436f6e746578746c766b51c3567e6c766b57c37e617c680f4e656f2e53746f726167652e4765746c766b58527ac46c766b57c36165910b6c766b58c36165880b7e6165830b6c766b52527ac462e100616168164e656f2e53746f726167652e476574436f6e746578746c766b51c3557e6c766b57c37e617c680f4e656f2e53746f726167652e4765746c766b59527ac46c766b59c36165a2086c766b57527ac46c766b53c351936c766b53527ac46c766b57c3c0009c6c766b5a527ac46c766b5ac36406006273006168164e656f2e53746f726167652e476574436f6e746578746c766b51c3567e6c766b57c37e617c680f4e656f2e53746f726167652e4765746c766b58527ac46c766b52c36c766b57c36165b20a6c766b58c36165a90a7e6165a40a7e6c766b52527ac461516c766b5b527ac4621aff6c766b53c36165b4096c766b52c37e6c766b56527ac46203006c766b56c3616c756654c56b6c766b00527ac46c766b51527ac4616c766b00c361656d046c766b52527ac46168164e656f2e53746f726167652e476574436f6e746578746c766b52c3587e6c766b51c37e617c680f4e656f2e53746f726167652e4765746c766b53527ac46203006c766b53c3616c756654c56b6c766b00527ac46c766b51527ac4616c766b00c36165ff036c766b52527ac46168164e656f2e53746f726167652e476574436f6e746578746c766b52c3597e6c766b51c37e617c680f4e656f2e53746f726167652e4765746c766b53527ac46203006c766b53c3616c756653c56b6c766b00527ac4616c766b00c3616598036c766b51527ac46168164e656f2e53746f726167652e476574436f6e746578746c766b51c3577e617c680f4e656f2e53746f726167652e4765746c766b52527ac46203006c766b52c3616c756656c56b6c766b00527ac46c766b51527ac4616c766b00c36165a9fa6c766b52527ac46c766b00c36165a1fc6c766b53527ac46c766b00c3616567ff6c766b54527ac46c766b52c36165e6086c766b53c36165dd087e6c766b54c36165d3087e6c766b55527ac46203006c766b55c3616c756653c56b6c766b00527ac46c766b51527ac4616c766b00c3616515ff6c766b51c3617c659a0a6c766b52527ac46203006c766b52c3616c756653c56b6c766b00527ac4616168164e656f2e53746f726167652e476574436f6e746578746c766b00c3617c680f4e656f2e53746f726167652e4765746c766b51527ac46c766b51c3c0641b006c766b51c300517f5100517f9c6307000062040051620400006c766b52527ac46203006c766b52c3616c756651c56b6c766b00527ac4616168164e656f2e53746f726167652e476574436f6e746578746c766b00c351615272680f4e656f2e53746f726167652e50757461616c756652c56b6c766b00527ac4616168164e656f2e53746f726167652e476574436f6e746578746c766b00c3547e617c680f4e656f2e53746f726167652e4765746c766b51527ac46203006c766b51c3616c756652c56b6c766b00527ac46c766b51527ac4616168164e656f2e53746f726167652e476574436f6e746578746c766b00c3547e6c766b51c3615272680f4e656f2e53746f726167652e50757461616c756652c56b6c766b00527ac4616168164e656f2e53746f726167652e476574436f6e746578746c766b00c3517e617c680f4e656f2e53746f726167652e4765746c766b51527ac46203006c766b51c3616c756652c56b6c766b00527ac46c766b51527ac4616168164e656f2e53746f726167652e476574436f6e746578746c766b00c3517e6c766b51c3615272680f4e656f2e53746f726167652e50757461616c756652c56b6c766b00527ac4616168164e656f2e53746f726167652e476574436f6e746578746c766b00c3577e617c680f4e656f2e53746f726167652e4765746c766b51527ac46203006c766b51c3616c756652c56b6c766b00527ac46c766b51527ac4616168164e656f2e53746f726167652e476574436f6e746578746c766b00c3577e6c766b51c3615272680f4e656f2e53746f726167652e50757461616c756653c56b6c766b00527ac4616c766b00c3c06410006c766b00c3c002ff00a0620400516c766b51527ac46c766b51c3640e00006c766b52527ac4621d006c766b00c3c06165390c6c766b00c37e6c766b52527ac46203006c766b52c3616c756653c56b6c766b00527ac4616c766b00c3c0519f6319006c766b00c3c06c766b00c300517f51939c009c620400516c766b51527ac46c766b51c3640e00006c766b52527ac4621c006c766b00c3516c766b00c300517f7f6c766b52527ac46203006c766b52c3616c756653c56b6c766b00527ac46c766b51527ac4616c766b00c3527e6c766b51c3617c6546076c766b52527ac46203006c766b52c3616c756653c56b6c766b00527ac46c766b51527ac4616c766b00c3527e6c766b51c3617c65a0086c766b52527ac46203006c766b52c3616c756653c56b6c766b00527ac46c766b51527ac46c766b52527ac4616168164e656f2e53746f726167652e476574436f6e746578746c766b00c3597e6c766b51c37e516c766b52c37e615272680f4e656f2e53746f726167652e50757461616c756653c56b6c766b00527ac46c766b51527ac46c766b52527ac4616168164e656f2e53746f726167652e476574436f6e746578746c766b00c3597e6c766b51c37e01006c766b52c37e615272680f4e656f2e53746f726167652e50757461616c756653c56b6c766b00527ac46c766b51527ac46c766b52527ac4616168164e656f2e53746f726167652e476574436f6e746578746c766b00c3587e6c766b51c37e6c766b52c3615272680f4e656f2e53746f726167652e50757461616c756653c56b6c766b00527ac46c766b51527ac4616c766b00c3557e6c766b51c3617c65be056c766b52527ac46203006c766b52c3616c756653c56b6c766b00527ac46c766b51527ac4616c766b00c3557e6c766b51c3617c6518076c766b52527ac46203006c766b52c3616c756652c56b6c766b00527ac4616168164e656f2e53746f726167652e476574436f6e746578746c766b00c3617c680f4e656f2e53746f726167652e4765746c766b51527ac46203006c766b51c3616c756652c56b6c766b00527ac46c766b51527ac4616168164e656f2e53746f726167652e476574436f6e746578746c766b00c36c766b51c3615272680f4e656f2e53746f726167652e50757461616c756653c56b6c766b00527ac4616c766b00c300547f61651c016c766b51527ac46c766b00c3546c766b51c37f6c766b52527ac46203006c766b52c3616c756654c56b6c766b00527ac4616c766b00c300547f6165df006c766b51527ac46c766b00c36c766b51c35593547f6165c6006c766b52527ac46c766b00c3596c766b51c3936c766b52c37f6c766b53527ac46203006c766b53c3616c756653c56b6c766b00527ac46c766b51527ac4616c766b00c36165dc0101017e6c766b51c36165d0017e6c766b52527ac46203006c766b52c3616c756653c56b6c766b00527ac46c766b51527ac4616168164e656f2e53746f726167652e476574436f6e746578746c766b00c36c766b51c37e617c680f4e656f2e53746f726167652e4765746c766b52527ac46203006c766b52c3616c756655c56b6c766b00527ac461006c766b51527ac4006c766b52527ac4623c00616c766b51c3020001956c766b51527ac46c766b51c36c766b00c36c766b52c3517f936c766b51527ac4616c766b52c351936c766b52527ac46c766b52c36c766b00c3c09f6c766b53527ac46c766b53c363afff6c766b51c36c766b54527ac46203006c766b54c3616c756657c56b6c766b00527ac4616c766b00c36c766b51527ac46c766b51c3020001976c766b52527ac46c766b51c36c766b52c394020001966c766b51527ac46c766b51c3020001976c766b53527ac46c766b51c36c766b53c394020001966c766b51527ac46c766b51c3020001976c766b54527ac46c766b51c36c766b54c394020001966c766b51527ac46c766b51c3020001976c766b55527ac46c766b55c36165ca066c766b54c36165c1067e6c766b53c36165b7067e6c766b52c36165ad067e6c766b56527ac46203006c766b56c3616c756657c56b6c766b00527ac4616c766b00c3c06c766b51527ac46c766b51c3020001976c766b52527ac46c766b51c36c766b52c394020001966c766b51527ac46c766b51c3020001976c766b53527ac46c766b51c36c766b53c394020001966c766b51527ac46c766b51c3020001976c766b54527ac46c766b51c36c766b54c394020001966c766b51527ac46c766b51c3020001976c766b55527ac46c766b55c36165f6056c766b54c36165ed057e6c766b53c36165e3057e6c766b52c36165d9057e6c766b00c37e6c766b56527ac46203006c766b56c3616c756653c56b6c766b00527ac46c766b51527ac4616168164e656f2e53746f726167652e476574436f6e746578746c766b00c3567e6c766b51c37e617c680f4e656f2e53746f726167652e4765746c766b52527ac46203006c766b52c3616c756654c56b6c766b00527ac46c766b51527ac46c766b52527ac46c766b53527ac4616168164e656f2e53746f726167652e476574436f6e746578746c766b00c3566c766b51c37e7e6c766b52c3c0616511056c766b52c36c766b53c37e7e615272680f4e656f2e53746f726167652e50757461616c756652c56b6c766b00527ac46c766b51527ac4616168164e656f2e53746f726167652e476574436f6e746578746c766b00c3566c766b51c37e7e617c68124e656f2e53746f726167652e44656c65746561616c756653c56b6c766b00527ac46c766b51527ac4616c766b00c3c06c766b51c3c0907c907c9e6311006c766b00c36c766b51c39c620400006c766b52527ac46203006c766b52c3616c756658c56b6c766b00527ac46c766b51527ac4616c766b00c3616576fa6c766b52527ac46c766b00c36c766b51c3617c65d1fb6c766b53527ac46c766b00c36c766b52c3617c65bbfb6c766b54527ac46c766b53c300a06c766b55527ac46c766b55c3640e00006c766b56527ac4621b016c766b52c3009c6c766b57527ac46c766b57c3645800616168164e656f2e53746f726167652e476574436f6e746578746c766b00c36c766b51c37e0000617c6516fb615272680f4e656f2e53746f726167652e507574616c766b00c36c766b51c3617c650bfa616162a500616168164e656f2e53746f726167652e476574436f6e746578746c766b00c36c766b51c37e6c766b52c300617c65bdfa615272680f4e656f2e53746f726167652e507574616168164e656f2e53746f726167652e476574436f6e746578746c766b00c36c766b52c37e6c766b54c36165e2f96c766b51c3617c6571fa615272680f4e656f2e53746f726167652e507574616c766b00c36c766b51c3617c6566f96161516c766b56527ac46203006c766b56c3616c75665fc56b6c766b00527ac46c766b51527ac4616c766b00c36c766b51c3617c6551fa6c766b52527ac46c766b52c3c0009c6c766b56527ac46c766b56c3640e00006c766b57527ac4626e02006c766b53527ac46c766b52c3616580f96c766b54527ac46c766b52c3616533f96c766b55527ac46c766b54c3c0009c6c766b58527ac46c766b58c364a400616c766b55c3c0009c6c766b59527ac46c766b59c3641800616c766b00c36c766b53c3617c659ff86161627300616c766b00c36c766b55c3617c65acf96c766b5a527ac46168164e656f2e53746f726167652e476574436f6e746578746c766b00c36c766b55c37e6c766b5ac36165a4f800617c6537f9615272680f4e656f2e53746f726167652e507574616c766b00c36c766b55c3617c652cf8616161624501616c766b55c3c0009c6c766b5b527ac46c766b5bc3646600616c766b00c36c766b54c3617c6520f96c766b5c527ac46168164e656f2e53746f726167652e476574436f6e746578746c766b00c36c766b54c37e006c766b5cc3616554f8617c65abf8615272680f4e656f2e53746f726167652e507574616162c900616c766b00c36c766b55c3617c65bdf86c766b5d527ac46c766b00c36c766b54c3617c65a7f86c766b5e527ac46168164e656f2e53746f726167652e476574436f6e746578746c766b00c36c766b54c37e6c766b55c36c766b5ec36165d7f7617c652ef8615272680f4e656f2e53746f726167652e507574616168164e656f2e53746f726167652e476574436f6e746578746c766b00c36c766b55c37e6c766b5dc3616553f76c766b54c3617c65e2f7615272680f4e656f2e53746f726167652e5075746161616168164e656f2e53746f726167652e476574436f6e746578746c766b00c36c766b51c37e6c766b53c3615272680f4e656f2e53746f726167652e50757461516c766b57527ac46203006c766b57c3616c756653c56b6c766b00527ac4614d0001000102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f202122232425262728292a2b2c2d2e2f303132333435363738393a3b3c3d3e3f404142434445464748494a4b4c4d4e4f505152535455565758595a5b5c5d5e5f606162636465666768696a6b6c6d6e6f707172737475767778797a7b7c7d7e7f808182838485868788898a8b8c8d8e8f909192939495969798999a9b9c9d9e9fa0a1a2a3a4a5a6a7a8a9aaabacadaeafb0b1b2b3b4b5b6b7b8b9babbbcbdbebfc0c1c2c3c4c5c6c7c8c9cacbcccdcecfd0d1d2d3d4d5d6d7d8d9dadbdcdddedfe0e1e2e3e4e5e6e7e8e9eaebecedeeeff0f1f2f3f4f5f6f7f8f9fafbfcfdfeff6c766b51527ac46c766b51c36c766b00c3517f6c766b52527ac46203006c766b52c3616c7566";
        codeAddress = "80b0cc71bda8653599c5666cae084bff587e2de1";
        ontSdk.setRestful(restUrl);
        ontSdk.setDefaultConnect(ontSdk.getRestful());
        ontSdk.openWalletFile("RecordTxTest.json");
        ontSdk.setCodeAddress("803ca638069742da4b6871fe3d7f78718eeee78a");


        if(ontSdk.getWalletMgr().getIdentitys().size() < 1) {

            ontSdk.getWalletMgr().createIdentity("passwordtest");
            ontSdk.getWalletMgr().writeWallet();
        }

        id = ontSdk.getWalletMgr().getIdentitys().get(0);
    }

    @Test
    public void sendPut() throws Exception {

//        String res = ontSdk.getRecordTx().sendPut(id.ontid.replace("did:ont:",""),"passwordtest","key","value");
//        Assert.assertNotNull(res);
    }

    @Test
    public void sendGet() throws Exception {

//        String res = ontSdk.getRecordTx().sendGet(id.ontid.replace("did:ont:",""),"passwordtest","key");
//        Assert.assertNotNull(res);
    }
}
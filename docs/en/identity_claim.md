<h1 align="center"> Digital Identity and Verifable Claim Management</h1>

<p align="center" class="version">Version 1.0.0 </p>

English / [中文](../cn/identity_claim.md)

## Introduction

Relevant descriptions of digital ID can be found in [ONT ID Protocol and Trust Framework](https://github.com/ontio/ontology-DID).

## Wallet file specification

A wallet file is a JSON data storage file that stores multiple digital identities and digital asset accounts.
You may refer to [Wallet File Specification](Wallet_File_Specification.md) for detailed information.

You need to create/open a wallet file to create a digital identity.


```
//If the wallet file does not exist, a wallet file will be auto-generated.
wm.openWalletFile("Demo3.json");
```

Note: Only a wallet file in the operating file format is currently supported, with extended support of database or other storage methods.



## Digital ID account management

* 1. Data structure

`ontid` A user’s identity.
`label` The name of a user ID.
`lock` Indicates whether the user’s ID is locked, whose default value is false. Locked ID info cannot get updated in the client.  
`controls` The array of identity ControlData.
`extra` The field that client developer stores extra information, whose value may be null.

```
//Identity data structure
public class Identity {
	public String label = "";
	public String ontid = "";
	public boolean isDefault = false;
	public boolean lock = false;
	public List<Control> controls = new ArrayList<Control>();
}
```


`algorithm` Encryption algorithm.
`parameters` The parameters used in the encryption algorithm.
`curve` Elliptic curve.
`id` The single identifier of control.
`key` NEP-2 private key.
`salt` Salt.
`hash` Hash algorithm.


```
public class Control {
    public String algorithm = "ECDSA";
    public Map parameters = new HashMap() ;
    public String id = "";
    public String key = "";
    public String salt = "";
    public String hash = "sha256";
    @JSONField(name = "enc-alg")
    public String encAlg = "aes-256-gcm";
    public String address = "";
}
```


* 2. Create a digital identity

Digital identity creation refers to generation of a digital identity with identity data structure and writing it to a wallet file.

```
Identity identity = ontSdk.getWalletMgr().createIdentity("password");
//The account or identity created is stored in the memory only and a write api is required to write it to the wallet file.
ontSdk.getWalletMgr().writeWallet();
```


- 3. Register blockchain-based identity

Only after successfully registering an identity with the blockchain can the identity be truly used.

There are two ways to register your identity with the chain:

**Method one**

Registrant specifies the account address for payment of transaction fees.

```
Identity identity = ontSdk.getWalletMgr().createIdentity(password);
ontSdk.nativevm().ontId().sendRegister(identity,password,payer,payerpwd,gaslimit,gasprice);
```

**Method two**

Send the constructed transaction to the server and let the server sign the transaction fee account.

```
Identity identity = ontSdk.getWalletMgr().createIdentity(password);
Transaction tx = ontSdk.nativevm().ontId().makeRegister(identity.ontid,password,salt,payerAcc.address,ontSdk.DEFAULT_GAS_LIMIT,0);
ontSdk.signTx(tx,identity.ontid,password,salt);
ontSdk.getConnect().sendRawTransaction(tx);
```

Upon successful registration, the corresponding DDO of the ONT ID will be stored in Ontology blockchain. Detailed information about DDO can be found in [ONT ID identity protocol and smart contract implementation]
https://github.com/ontio/ontology-DID/blob/master/README.md


* 4. Import account or identity

Users who have already created a digital identity or account may import it into a wallet file from the SDK.

**Note:** It is advised to check if an identity already exists on the blockchain before you import one. If DDO does not exist, it means that no such identity has been registered on the blockchain. Then you may need to use ontSdk.getOntIdTx().sendRegister(identity,"passwordtest") for registration.

```
Identity identity = ontSdk.getWalletMgr().importIdentity(encriptPrivateKey,password,salt,address);
//write to wallet     
ontSdk.getWalletMgr().writeWallet();
```

Parameter Descriptions:

encriptPrivateKey: Encrypted private key.
password: Password used to encrypt the private key.
salt: Private key decryption parameters.
address: Account address.

* 5. Query blockchain-based identity

DDO of blockchain-based identity can be queried by entering ONT ID.


```
//get DDO by entering ONT ID
String ddo = ontSdk.nativevm().ontId().sendGetDDO(ontid);

//return in DDO format
{
	"Attributes": [{
		"Type": "String",
		"Value": "value1",
		"Key": "key1"
	}],
	"OntId": "did:ont:TA5UqF8iPqecMdBzTdzzANVeY8HW1krrgy",
	"Recovery": "TA6AhqudP1dcLknEXmFinHPugDdudDnMJZ",
	"Owners": [{
		"Type": "ECDSA",
		"Curve": "P256",
		"Value": "12020346f8c238c9e4deaf6110e8f5967cf973f53b778ed183f4a6e7571acd51ddf80e",
		"PubKeyId": "did:ont:TA5UqF8iPqecMdBzTdzzANVeY8HW1krrgy#keys-1"
	}, {
		"Type": "ECDSA",
		"Curve": "P256",
		"Value": "1202022fabd733d7d7d7009125bfde3cb0afe274769c78fd653079ecd5954ae9f52644",
		"PubKeyId": "did:ont:TA5UqF8iPqecMdBzTdzzANVeY8HW1krrgy#keys-2"
	}]
}
```

* 6. Remove identity


```
ontSdk.getWalletMgr().getWallet().removeIdentity(ontid);
//wrote to wallet
ontSdk.getWalletMgr().writeWallet();
```


* 7. Set default account or identity


```
ontSdk.getWalletMgr().getWallet().setDefaultIdentity(index);
ontSdk.getWalletMgr().getWallet().setDefaultIdentity(ontid);
```

* 8. Update blockchain-based DDO attribute

**Method one**

Specifies the account address for payment of transaction fees.

```
//update an attribute
String sendAddAttributes(String ontid, String password,byte[] salt, Attribute[] attributes,Account payerAcct,long gaslimit,long gasprice)
```


| Param   | Field   | Type  | Description |      Remarks |
| ----- | ------- | ------ | ------------- | ----------- |
| Input param | password| String | Publisher's address | Required, password to decrypt private key|
|   | ontid    | String | Name of asset | Required, ID |
|   | salt     | byte[] | |Required|
|        | attributes | Attribute[]|  Attribute array | Required |
|        | payerAcct    | Account | Payment transaction account    |  Required |
|           | gaslimit      | long | Gas limit     | Required |
|           | gasprice      | long | Gas price    | Required |
| Output param | txhash   | String  | Transaction hash | 64-bit string |


**Method two**

Send the constructed transaction to the server and let the server sign the transaction fee account.
```
Transaction makeAddAttributes(String ontid, String password,byte[] salt, Attribute[] attributes,String payer,long gaslimit,long gasprice)
```

Example:
```
Transaction tx = ontSdk.nativevm().ontId().makeAddAttributes(ontid,password,salt,attributes,payer,gaslimit,0);
ontSdk.signTx(tx,identity.ontid.replace(Common.didont,""),password);
ontSdk.getConnectMgr().sendRawTransaction(tx);
```

* 9. Remove blockchain-based DDO attribute

**Method one**

```
String sendRemoveAttribute(String ontid,String password,salt,String path,Account payerAcct,long gaslimit,long gasprice)
```

| Param        | Field   | Type   | Description  |       Remarks       |
| -----        | ------- | ------ | ------------- | ------------------- |
| Input param  | password| String | Publisher's address | Required, password to decrypt private key |
|   | ontid    | String | Name of asset | Required, ID |
|   | salt     | byte[] | |    required  |
|   | path    | String | Path       | Required |
|   | payerAcct    | Account  |Payment transaction account  | Required，payer |
|   | gaslimit      | long | Gas limit     | Required |
|   | gasprice      | long | Gas price    | Required |
| Output param | txhash   | String  | transaction hash | 64-bit string |

**Method two**

Send the constructed transaction to the server and let the server sign the transaction fee account.
```
Transaction makeRemoveAttribute(String ontid,String password,byte[] salt,String path,String payer,long gaslimit,long gasprice)
```


Example:
```
Transaction tx = ontSdk.nativevm().ontId().makeRemoveAttribute(ontid,password,salt,path,payer,gaslimit,0);
ontSdk.signTx(tx,identity.ontid,password);
ontSdk.getConnectMgr().sendRawTransaction(tx);
```


* Add publicKey

**Method one**

```
String sendAddPubKey(String ontid, String password,byte[] salt, String newpubkey,Account payerAcct,long gaslimit,long gasprice)
```

| Param      | Field   | Type  | Description |             Remarks |
| ----- | ------- | ------ | ------------- | ----------- |
| Input param| password| String | Identity password | Required |
|         | salt     | byte[] | |    Required  |
|        | ontid    | String | Identity ID   | Required, identity ID |
|        | newpubkey| String  |Public key       | Required, new pubkey|
|        | payerAcct    | Account  | Payer account       | Required, payer |
|        | gaslimit      | long | Gas limit     | Required |
|        | gasprice      | long | Gas price    | Required |
| Output param | txhash   | String  | Transaction hash  | Transaction hash |


**Method two**

Send the constructed transaction to the server and let the server sign the transaction fee account.
```
Transaction makeAddPubKey(String ontid,String password,String newpubkey,String payer,long gaslimit,long gasprice)
```

Example:
```
Transaction tx = ontSdk.nativevm().ontId().makeAddPubKey(ontid,password,salt,newpubkey,payer,gas);
ontSdk.signTx(tx,identity.ontid.replace(Common.didont,""),password);
ontSdk.getConnectMgr().sendRawTransaction(tx);
```

**Method three (recovery)**

```
String sendAddPubKey(String ontid,String recoveryAddr, String password,byte[] salt, String newpubkey,Account payerAcct,long gaslimit,long gasprice)
```


| Param      | Field   | Type  | Description |             Remarks |
| ----- | ------- | ------ | ------------- | ----------- |
| Input param|ontid|String | Identity ID   | Required，identity ID |
|        | recoveryAddr| String | Recovery address | Required |
|        | password| String | Recovery password | Required |
|        | salt    | byte[] |                   | Required |
|        | newpubkey| String  |Public key       | Required, new pubkey|
|        | payerAcct | Account  | Payer       | Required, payer |
|        | gaslimit      | long | Gas limit     | Required |
|        | gasprice      | long | Gas price    | Required |
| Output param | txhash   | String  | Transaction hash  | Transaction hash |


**Method four (recovery)**
```
Transaction makeAddPubKey(String ontid,String recoveryAddr,String password,String newpubkey,String payer,long gaslimit,long gasprice)
```

Parameter description, please refer to method three (recovery)


* Remove publicKey

**Method one**

```
String sendRemovePubKey(String ontid, String password,,byte[] salt, String removePubkey,Account payerAcct,long gaslimit,long gasprice)
```


| Param      | Field   | Type  | Description |             Remarks |
| ----- | ------- | ------ | ------------- | ----------- |
| Input param | password| String | Identity password | rRquired |
|        | salt | byte[] | | Required|
|        | ontid    | String | Identity ID   | Required，identity ID |
|        | removePubkey| String  |Public key       | Required, removePubkey|
|        | payerAcct    | Account  | Payer account       | Required，payer |
|   | gaslimit      | long | gaslimit     | Required |
|   | gasprice      | long | gas price    | Required |
| Output param | txhash   | String  | Transaction hash  | Transaction hash |


**Method two**

Send the constructed transaction to the server and let the server sign the transaction fee account.

```
Transaction tx = ontSdk.nativevm().ontId().makeRemovePubKey(ontid,password,salt,removePubkey,payer,gas);
ontSdk.signTx(tx,identity.ontid,password);
ontSdk.getConnectMgr().sendRawTransaction(tx);
```

**Method three (recovery)**

```
String sendRemovePubKey(String ontid, String recoveryAddr,String password,salt, String removePubkey,Account payerAcct,long gaslimit,long gasprice)
```

| Param      | Field   | Type  | Description |             Remarks |
| ----- | ------- | ------ | ------------- | ----------- |
| Input param | ontid    | String | Identity ID   | Required, identity ID |
|        | password| String | Identity password | Required |
|        | salt| byte[] |  | Required |
|        | recoveryAddr| String | Recovery password | Required |
|        | removePubkey| String  |Public key       | Required. remove pubkey|
|        | payerAcct    | Account  | Payer       | Required, payer |
|   | gaslimit      | long | Gas limit     | Required |
|   | gasprice      | long | Gas price    | Required |
| Output param | txhash   | String  | Transaction hash  | Transaction hash |


**Method four (recovery)**

```
Transaction makeRemovePubKey(String ontid,String recoveryAddr, String password,salt, String removePubkey,String payer,long gaslimit,long gasprice)
```

Parameter description, please refer to method four (recovery)

* Add recovery

**Method one**

```
String sendAddRecovery(String ontid, String password,byte[] salt, String recoveryAddr,Account payerAcct,long gaslimit,long gasprice)
```

| Param      | Field   | Type  | Description |             Remarks |
| ----- | ------- | ------ | ------------- | ----------- |
| Input param | password| String | Identity password | Required |
|        | ontid    | String | Identity ID   | Required, identity ID |
|        | password | String | Identity password |Required|
|        | salt     | byte[] | Identity salt  | Required|
|        | recoveryAddr| String  |Recovery address | Required, recovery|
|        | payerAcct    | Account  | Payer       | Required, payer |
|   | gaslimit      | long | Gas limit     | Required |
|   | gasprice      | long | Gas price    | Required |
| Output param | txhash   | String  | Transaction hash  | Transaction hash |


**Method two**

Send the constructed transaction to the server and let the server sign the transaction fee account.

```
Transaction makeAddRecovery(String ontid, String password,salt, String recoveryAddr,String payer,long gaslimit,long gasprice)
```

Example:
```
Transaction tx = ontSdk.nativevm().ontId().makeAddRecovery(ontid,password,salt,recovery,payer,gas);
ontSdk.signTx(tx,identity.ontid,password);
ontSdk.getConnectMgr().sendRawTransaction(tx);
```

* Recovery


```
String sendChangeRecovery(String ontid, String newRecovery, String oldRecovery, String password,salt,long gaslimit,long gasprice)
```

| Param      | Field   | Type  | Description |             Remarks |
| ----- | ------- | ------ | ------------- | ----------- |
| Input param | ontid    | String | Identity ID   | required，identity ID |
|        | newRecovery| String  |New recovery address | Required，newRecovery|
|        | oldRecovery| String  |Old recovery address | Required，oldRecovery|
|        |  password | String  | Old recovery password  | Required |
|        | salt| byte[] |  | Required |
|   | gaslimit      | long | Gas limit     | Required |
|   | gasprice      | long | Gas price    | Required |
| Output param | txhash   | String  | Transaction hash  | Transaction hash |



## Verifiable claim

### 1. Data structure specification

* Claim has the following data structure:

```
class Claim{
  header : Header
  payload : Payload
  signature : byte[]
}
```


```
class Header {
    public String Alg = "ONT-ES256";
    public String Typ = "JWT-X";
    public String Kid;
    }
```

`alg` attribute specifies the signature scheme to use. A list of supported values can be found here.
`typ` attribute can take one of the two values:

    * JWT: This corresponds to the case that blockchain proof is not contained in the claim.
    * JWT-X: This corresponds to the case that blockchain proof is a part of the claim.

`kid`  attribute refers to the public key used for signature verification. It has the form <ontID>#keys-<id> as defined in ONT ID specification.


```
class Payload {
    public String Ver;
    public String Iss;
    public String Sub;
    public long Iat;
    public long Exp;
    public String Jti;
    @JSONField(name = "@context")
    public String Context;
    public Map<String, Object> ClmMap = new HashMap<String, Object>();
    public Map<String, Object> ClmRevMap = new HashMap<String, Object>();
    }
```

`ver` attribute specifies the version of the claim spec it follows.

`iss` attribute refers to the ONT ID of the issuer.

`sub` attribute refers to the ONT ID of the recipient.

`iat` attribute marks the time the claim was created and has the format of unix timestamp.

`exp` attribute marks the expiration time of the claim and has the format of unix timestamp.

`jti` attribute specifies the unique identifier of the verifiable claim.

`@context` attribute specifies the uri of claim content definition document which defines the meaning of each field and the type of the value.

`clm` attribute is an object which contains the claim content.

`clm-rev` attribute is an object which defines the revocation mechanism the claim use.



### 2. Interface list

1. createOntIdClaim (String signerOntid, String password,byte[] salt, String context, Map<String, Object> claimMap, Map metaData,Map clmRevMap,long expire)

     Function description: Create claim

    | Parameter      | Field   | Type  | Description |            Remarks |
    | ----- | ------- | ------ | ------------- | ----------- |
    | Input parameter | signerOntid| String | ONT ID | Required |
    |        | password    | String | ONT ID password   | Required |
    |        | salt        | byte[] | Private key decryption parameters |Required|
    |        | context| String  |Attribute specifies the URI of claim content definition document which defines the meaning of each field and the type of the value | Required|
    |        | claimMap| Map  |Content of claim | Required|
    |        | metaData   | Map | Claim issuer and subject's ONT ID | Required |
    |        | clmRevMap   | Map | Attribute is an object which defines the revocation mechanism the claim use | Required |
    |        | expire   | long | Attribute marks the expiration time of the claim and has the format of unix timestamp     | required |
    | Output parameter| claim   | String  |   |  |

    Refer to: https://github.com/kunxian-xia/ontology-DID/blob/master/docs/en/claim_spec.md

2. boolean verifyOntIdClaim (string claim)

    Function description: Verify claim

    | Parameter      | Field   | Type  | Description |            Remarks |
    | ----- | ------- | ------ | ------------- | ----------- |
    | Input parameter | claim| String | Trusted claim | Required |
    | Output parameter | true or false   | boolean  |   |  |


### 3. Sign and issue verifiable claim
Verifiable claim is constructed based on user input, which contains signed data.

```
Map<String, Object> map = new HashMap<String, Object>();
map.put("Issuer", dids.get(0).ontid);
map.put("Subject", dids.get(1).ontid);
Map clmRevMap = new HashMap();
clmRevMap.put("typ","AttestContract");
clmRevMap.put("addr",dids.get(1).ontid.replace(Common.didont,""));
String claim = ontSdk.nativevm().ontId().createOntIdClaim(dids.get(0).ontid,password,salt, "claim:context", map, map,clmRevMap,System.currentTimeMillis()/1000 +100000);
```
> Note: The issuer may have multiple public keys. The parameter ontid of createOntIdClaim specifies which public key to use.

### 3. Verify verifiable claim

```
boolean b = ontSdk.nativevm().ontId().verifyOntIdClaim(claim);
```


### 4. Use cases


```
Identity identity = ontSdk.getWalletMgr().createIdentity(password);
ontSdk.nativevm().ontId().sendRegister(identity2,password,salt,payerAcc,ontSdk.DEFAULT_GAS_LIMIT,0);
String ontid = ident.ontid;
Map recordMap = new HashMap();
recordMap.put("key0", "world0");
recordMap.put("keyNum", 1234589);
recordMap.put("key2", false);
String hash = ontSdk.nativevm().ontId().sendAddAttributes(dids.get(0).ontid,password,salt,attributes,payerAcc,ontSdk.DEFAULT_GAS_LIMIT,0);
```
Note: When the attribute does not exist, calling the sendUpdateAttribute method will increase the corresponding attribute. When the attribute exists, the corresponding attribute will be updated. Attri represents the attribute name, "Json" is the attribute value data type, and recordMap represents the attribute value.

Claim issuance and verification:

```
Map<String, Object> map = new HashMap<String, Object>();
map.put("Issuer", dids.get(0).ontid);
map.put("Subject", dids.get(1).ontid);

Map clmRevMap = new HashMap();
clmRevMap.put("typ","AttestContract");
clmRevMap.put("addr",dids.get(1).ontid.replace(Common.didont,""));

String claim = ontSdk.nativevm().ontId().createOntIdClaim(dids.get(0).ontid,password,salt, "claim:context", map, map,clmRevMap,System.currentTimeMillis()/1000 +100000);
boolean b = ontSdk.nativevm().ontId().verifyOntIdClaim(claim);
```

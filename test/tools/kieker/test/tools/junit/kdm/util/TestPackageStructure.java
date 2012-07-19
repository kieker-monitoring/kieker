package kieker.test.tools.junit.kdm.util;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.gmt.modisco.omg.kdm.code.ArrayType;
import org.eclipse.gmt.modisco.omg.kdm.code.CharType;
import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.CodeFactory;
import org.eclipse.gmt.modisco.omg.kdm.code.CodeModel;
import org.eclipse.gmt.modisco.omg.kdm.code.CodeRelationship;
import org.eclipse.gmt.modisco.omg.kdm.code.ExportKind;
import org.eclipse.gmt.modisco.omg.kdm.code.Extends;
import org.eclipse.gmt.modisco.omg.kdm.code.Implements;
import org.eclipse.gmt.modisco.omg.kdm.code.Imports;
import org.eclipse.gmt.modisco.omg.kdm.code.IndexUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.IntegerType;
import org.eclipse.gmt.modisco.omg.kdm.code.InterfaceUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.ItemUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.MethodKind;
import org.eclipse.gmt.modisco.omg.kdm.code.MethodUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.Package;
import org.eclipse.gmt.modisco.omg.kdm.code.ParameterKind;
import org.eclipse.gmt.modisco.omg.kdm.code.ParameterUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.Signature;
import org.eclipse.gmt.modisco.omg.kdm.code.StorableKind;
import org.eclipse.gmt.modisco.omg.kdm.code.StorableUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.VoidType;
import org.eclipse.gmt.modisco.omg.kdm.code.impl.CodeFactoryImpl;
import org.eclipse.gmt.modisco.omg.kdm.kdm.Attribute;
import org.eclipse.gmt.modisco.omg.kdm.kdm.KdmFactory;
import org.eclipse.gmt.modisco.omg.kdm.kdm.Segment;
import org.eclipse.gmt.modisco.omg.kdm.kdm.impl.KdmFactoryImpl;

/**
 * This class provides a little test structure, which contains some packages, classes,
 * interfaces and methods with parameter. The structure looks like:
 * 
 * test.eins
 * test.zwei.drei.vier
 * test.zwei.fuenf.Main.main(char[] args) : void
 * test.zwei.sechs.IIterator.iterate() : int
 * test.zwei.sieben.Foo.Bar
 * test.zwei.acht.IPrintable.IWritable
 * test.zwei.neun.InterfaceClass.IGetable
 * test.zwei.zehn.ISetable.ClassInterface
 * org.emf.kdm.Demo.showA(int count) : void
 * org.emf.kdm.Demo.showB(char letter, int count) : void
 * org.emf.kdm.Demo.showC() : int
 * org.emf.kdm.Demo.set(Item item) : void
 * org.emf.kdm.Manager.Manager() // constructor
 * org.emf.abc.Input.Stream.open(char[] name, count) : int
 * GlobalClass
 * IGlobalInterface
 * globalMethod() : void
 * globalMethodNext(test.zwei.acht.IPrintable letter) : void
 * 
 * calls:
 * test.zwei.fuenf.Main.main() ==> org.emf.kdm.Demo.showA()
 * test.zwei.fuenf.Main.main() ==> org.emf.abc.Input.Stream.open()
 * test.zwei.fuenf.Main.main() ==> globalMethod()
 * org.emf.kdm.Demo.showB() ==> org.emf.abc.Input.Stream.open()
 * 
 * dependencies:
 * test.zwei.acht.IPrintable.IWritable extends test.zwei.sechs.IIterator
 * test.zwei.fuenf.Main imports test.zwei.acht.IPrintable
 * GlobalClass extends test.zwei.sieben.Foo
 * org.emf.kdm.Manager imports org.emf.abc.Input.Stream
 * org.emf.kdm.Manager implements test.zwei.zehn.ISetable
 * org.emf.kdm.Manager extends test.zwei.neun.InterfaceClass
 * 
 * attributes:
 * test.zwei.zehn.ISetable:
 * private int length
 * protected char[] letter
 * public static test.zwei.sieben.Foo dings
 * 
 * @author Benjamin Harms
 * 
 */
public final class TestPackageStructure {
	private static KdmFactory kdmFactory = new KdmFactoryImpl();
	private static CodeFactory codeFactory = new CodeFactoryImpl();
	private static CodeModel codeModel = TestPackageStructure.codeFactory.createCodeModel();
	private static Segment segment = TestPackageStructure.kdmFactory.createSegment();
	private static Map<String, Package> packages = new HashMap<String, Package>();
	private static Map<String, ClassUnit> classes = new HashMap<String, ClassUnit>();
	private static Map<String, InterfaceUnit> interfaces = new HashMap<String, InterfaceUnit>();
	private static Map<String, MethodUnit> methods = new HashMap<String, MethodUnit>();

	/**
	 * Setup the test data.
	 */
	static {
		// Generate packages
		TestPackageStructure.segment.getModel().add(TestPackageStructure.codeModel);
		final Package pEins, pZwei, pDrei, pVier, pFuenf, pSechs, pSieben, pAcht, pNeun, pZehn, pTest, pKdm, pOrg, pEmf, pAbc;

		pEins = TestPackageStructure.codeFactory.createPackage();
		pEins.setName("eins");
		pZwei = TestPackageStructure.codeFactory.createPackage();
		pZwei.setName("zwei");
		pDrei = TestPackageStructure.codeFactory.createPackage();
		pDrei.setName("drei");
		pVier = TestPackageStructure.codeFactory.createPackage();
		pVier.setName("vier");
		pFuenf = TestPackageStructure.codeFactory.createPackage();
		pFuenf.setName("fuenf");
		pSechs = TestPackageStructure.codeFactory.createPackage();
		pSechs.setName("sechs");
		pSieben = TestPackageStructure.codeFactory.createPackage();
		pSieben.setName("sieben");
		pAcht = TestPackageStructure.codeFactory.createPackage();
		pAcht.setName("acht");
		pNeun = TestPackageStructure.codeFactory.createPackage();
		pNeun.setName("neun");
		pZehn = TestPackageStructure.codeFactory.createPackage();
		pZehn.setName("zehn");
		pTest = TestPackageStructure.codeFactory.createPackage();
		pTest.setName("test");
		pKdm = TestPackageStructure.codeFactory.createPackage();
		pKdm.setName("kdm");
		pOrg = TestPackageStructure.codeFactory.createPackage();
		pOrg.setName("org");
		pEmf = TestPackageStructure.codeFactory.createPackage();
		pEmf.setName("emf");
		pAbc = TestPackageStructure.codeFactory.createPackage();
		pAbc.setName("abc");

		// Setup package map
		TestPackageStructure.packages.put("test", pTest);
		TestPackageStructure.packages.put("test.eins", pEins);
		TestPackageStructure.packages.put("test.zwei", pZwei);
		TestPackageStructure.packages.put("test.zwei.drei", pDrei);
		TestPackageStructure.packages.put("test.zwei.drei.vier", pVier);
		TestPackageStructure.packages.put("test.zwei.fuenf", pFuenf);
		TestPackageStructure.packages.put("test.zwei.sechs", pSechs);
		TestPackageStructure.packages.put("test.zwei.sieben", pSieben);
		TestPackageStructure.packages.put("test.zwei.acht", pAcht);
		TestPackageStructure.packages.put("test.zwei.neun", pNeun);
		TestPackageStructure.packages.put("test.zwei.zehn", pZehn);
		TestPackageStructure.packages.put("org", pOrg);
		TestPackageStructure.packages.put("org.emf", pEmf);
		TestPackageStructure.packages.put("org.emf.kdm", pKdm);
		TestPackageStructure.packages.put("org.emf.abc", pAbc);

		// Generate classes
		ClassUnit cMain, cFoo, cBar, cInterfaceClass, cClassInterface, cDemo, cManager, cInput, cStream, cGlobalClass;
		cMain = TestPackageStructure.codeFactory.createClassUnit();
		cMain.setName("Main");
		cFoo = TestPackageStructure.codeFactory.createClassUnit();
		cFoo.setName("Foo");
		cBar = TestPackageStructure.codeFactory.createClassUnit();
		cBar.setName("Bar");
		cInterfaceClass = TestPackageStructure.codeFactory.createClassUnit();
		cInterfaceClass.setName("InterfaceClass");
		cClassInterface = TestPackageStructure.codeFactory.createClassUnit();
		cClassInterface.setName("ClassInterface");
		cDemo = TestPackageStructure.codeFactory.createClassUnit();
		cDemo.setName("Demo");
		cManager = TestPackageStructure.codeFactory.createClassUnit();
		cManager.setName("Manager");
		cInput = TestPackageStructure.codeFactory.createClassUnit();
		cInput.setName("Input");
		cStream = TestPackageStructure.codeFactory.createClassUnit();
		cStream.setName("Stream");
		cGlobalClass = TestPackageStructure.codeFactory.createClassUnit();
		cGlobalClass.setName("GlobalClass");

		// Setup class map
		TestPackageStructure.classes.put("test.zwei.fuenf.Main", cMain);
		TestPackageStructure.classes.put("test.zwei.sieben.Foo", cFoo);
		TestPackageStructure.classes.put("test.zwei.sieben.Foo.Bar", cBar);
		TestPackageStructure.classes.put("test.zwei.neun.InterfaceClass", cInterfaceClass);
		TestPackageStructure.classes.put("test.zwei.zehn.ISetable.ClassInterface", cClassInterface);
		TestPackageStructure.classes.put("org.emf.kdm.Demo", cDemo);
		TestPackageStructure.classes.put("org.emf.kdm.Manager", cManager);
		TestPackageStructure.classes.put("org.emf.abc.Input", cInput);
		TestPackageStructure.classes.put("org.emf.abc.Input.Stream", cStream);
		TestPackageStructure.classes.put("GlobalClass", cGlobalClass);

		// Generate Interfaces
		InterfaceUnit iIterator, iPrintable, iWritable, iGetable, iSetable, iGloablInterface;
		iIterator = TestPackageStructure.codeFactory.createInterfaceUnit();
		iIterator.setName("IIterator");
		iPrintable = TestPackageStructure.codeFactory.createInterfaceUnit();
		iPrintable.setName("IPrintable");
		iWritable = TestPackageStructure.codeFactory.createInterfaceUnit();
		iWritable.setName("IWritable");
		iGetable = TestPackageStructure.codeFactory.createInterfaceUnit();
		iGetable.setName("IGetable");
		iSetable = TestPackageStructure.codeFactory.createInterfaceUnit();
		iSetable.setName("ISetable");
		iGloablInterface = TestPackageStructure.codeFactory.createInterfaceUnit();
		iGloablInterface.setName("IGlobalInterface");

		// Setup interface map
		TestPackageStructure.interfaces.put("test.zwei.sechs.IIterator", iIterator);
		TestPackageStructure.interfaces.put("test.zwei.acht.IPrintable", iPrintable);
		TestPackageStructure.interfaces.put("test.zwei.acht.IPrintable.IWritable", iWritable);
		TestPackageStructure.interfaces.put("test.zwei.neun.InterfaceClass.IGetable", iGetable);
		TestPackageStructure.interfaces.put("test.zwei.zehn.ISetable", iSetable);
		TestPackageStructure.interfaces.put("IGlobalInterface", iGloablInterface);

		// Generate dependencies
		Imports importManager, importsMain;
		Implements implementsManager;
		Extends extendsIWritable, extendsManager, extendsGlobalClass;
		importManager = TestPackageStructure.codeFactory.createImports();
		importManager.setFrom(cManager);
		importManager.setTo(cStream);
		importsMain = TestPackageStructure.codeFactory.createImports();
		importsMain.setFrom(cMain);
		importsMain.setTo(iPrintable);
		extendsIWritable = TestPackageStructure.codeFactory.createExtends();
		extendsIWritable.setFrom(iWritable);
		extendsIWritable.setTo(iIterator);
		implementsManager = TestPackageStructure.codeFactory.createImplements();
		implementsManager.setFrom(cManager);
		implementsManager.setTo(iSetable);
		extendsManager = TestPackageStructure.codeFactory.createExtends();
		extendsManager.setFrom(cManager);
		extendsManager.setTo(cInterfaceClass);
		extendsGlobalClass = TestPackageStructure.codeFactory.createExtends();
		extendsGlobalClass.setFrom(cGlobalClass);
		extendsGlobalClass.setTo(cFoo);

		// Generate methods
		MethodUnit mMain, mIterate, mShowA, mShowB, mShowC, mManager, mSet, mOpen, mGlobalMethod, mGlobalMaethodNext;
		mMain = TestPackageStructure.codeFactory.createMethodUnit();
		mMain.setName("main()");
		mMain.setKind(MethodKind.METHOD);
		mIterate = TestPackageStructure.codeFactory.createMethodUnit();
		mIterate.setName("iterate()");
		mIterate.setKind(MethodKind.METHOD);
		mShowA = TestPackageStructure.codeFactory.createMethodUnit();
		mShowA.setName("showA()");
		mShowA.setKind(MethodKind.METHOD);
		mShowB = TestPackageStructure.codeFactory.createMethodUnit();
		mShowB.setName("showB()");
		mShowB.setKind(MethodKind.METHOD);
		mShowC = TestPackageStructure.codeFactory.createMethodUnit();
		mShowC.setName("showC()");
		mShowC.setKind(MethodKind.METHOD);
		mManager = TestPackageStructure.codeFactory.createMethodUnit();
		mManager.setName("Manager()");
		mManager.setKind(MethodKind.CONSTRUCTOR);
		mSet = TestPackageStructure.codeFactory.createMethodUnit();
		mSet.setName("set()");
		mSet.setKind(MethodKind.METHOD);
		mOpen = TestPackageStructure.codeFactory.createMethodUnit();
		mOpen.setName("open()");
		mOpen.setKind(MethodKind.METHOD);
		mGlobalMethod = TestPackageStructure.codeFactory.createMethodUnit();
		mGlobalMethod.setName("globalMethod()");
		mGlobalMethod.setKind(MethodKind.METHOD);
		mGlobalMaethodNext = TestPackageStructure.codeFactory.createMethodUnit();
		mGlobalMaethodNext.setName("globalMethodNext()");
		mGlobalMaethodNext.setExport(ExportKind.PROTECTED);

		// Setup method map
		TestPackageStructure.methods.put("test.zwei.fuenf.Main.main()", mMain);
		TestPackageStructure.methods.put("test.zwei.sechs.IIterator.iterate()", mIterate);
		TestPackageStructure.methods.put("org.emf.kdm.Demo.showA()", mShowA);
		TestPackageStructure.methods.put("org.emf.kdm.Demo.showB()", mShowB);
		TestPackageStructure.methods.put("org.emf.kdm.Demo.showC()", mShowC);
		TestPackageStructure.methods.put("org.emf.kdm.Manager.Manager()", mManager);
		TestPackageStructure.methods.put("org.emf.kdm.Demo.set()", mSet);
		TestPackageStructure.methods.put("org.emf.abc.Input.Stream.open()", mOpen);
		TestPackageStructure.methods.put("globalMethod()", mGlobalMethod);
		TestPackageStructure.methods.put("globalMethodNext()", mGlobalMaethodNext);

		// Method calls
		CodeRelationship crMain1, crMain2, crMain3, crShowB;
		crMain1 = TestPackageStructure.codeFactory.createCodeRelationship();
		crMain1.setFrom(mMain);
		crMain1.setTo(mShowA);
		mMain.getCodeRelation().add(crMain1);
		crMain2 = TestPackageStructure.codeFactory.createCodeRelationship();
		crMain2.setFrom(mMain);
		crMain2.setTo(mOpen);
		mMain.getCodeRelation().add(crMain2);
		crMain3 = TestPackageStructure.codeFactory.createCodeRelationship();
		crMain3.setFrom(mMain);
		crMain3.setTo(mGlobalMethod);
		mMain.getCodeRelation().add(crMain3);
		crShowB = TestPackageStructure.codeFactory.createCodeRelationship();
		crShowB.setFrom(mShowB);
		crShowB.setTo(mOpen);
		mShowB.getCodeRelation().add(crShowB);

		// Setup signatures
		Signature sMain, sIterate, sShowA, sShowB, sShowC, sManager, sSet, sOpen, sGlobalMethod, sGlobalMethodNext;
		sMain = TestPackageStructure.codeFactory.createSignature();
		sIterate = TestPackageStructure.codeFactory.createSignature();
		sShowA = TestPackageStructure.codeFactory.createSignature();
		sShowB = TestPackageStructure.codeFactory.createSignature();
		sShowC = TestPackageStructure.codeFactory.createSignature();
		sManager = TestPackageStructure.codeFactory.createSignature();
		sSet = TestPackageStructure.codeFactory.createSignature();
		sOpen = TestPackageStructure.codeFactory.createSignature();
		sGlobalMethod = TestPackageStructure.codeFactory.createSignature();
		sGlobalMethodNext = TestPackageStructure.codeFactory.createSignature();

		// Types
		VoidType tVoidType;
		tVoidType = TestPackageStructure.codeFactory.createVoidType();
		tVoidType.setName("void");

		IntegerType tIntegerType;
		tIntegerType = TestPackageStructure.codeFactory.createIntegerType();
		tIntegerType.setName("int");

		CharType tCharType;
		tCharType = TestPackageStructure.codeFactory.createCharType();
		tCharType.setName("char");

		ClassUnit tItemType;
		tItemType = TestPackageStructure.codeFactory.createClassUnit();
		tItemType.setName("Item");

		ArrayType tCharArrayType;
		tCharArrayType = TestPackageStructure.codeFactory.createArrayType();
		tCharArrayType.setName("char[]");
		final IndexUnit indexUnit = TestPackageStructure.codeFactory.createIndexUnit();
		final ItemUnit itemUnit = TestPackageStructure.codeFactory.createItemUnit();
		indexUnit.setType(tIntegerType);
		itemUnit.setType(tCharType);
		tCharArrayType.setIndexUnit(indexUnit);
		tCharArrayType.setItemUnit(itemUnit);

		// Parameter units
		ParameterUnit puReturnMain, puReturnIterate, puReturnShowA, puReturnShowB, puReturnShowC, puReturnSet, puReturnGlobalMethod, puReturnOpen, puReturnGlobalMethodNext;
		ParameterUnit puMain, puShowA, puShowB1, puShowB2, puSet, puOpen1, puOpen2, puGlobalMethodNext;
		puReturnMain = TestPackageStructure.codeFactory.createParameterUnit();
		puReturnMain.setKind(ParameterKind.RETURN);
		puReturnMain.setType(tVoidType);
		puReturnIterate = TestPackageStructure.codeFactory.createParameterUnit();
		puReturnIterate.setKind(ParameterKind.RETURN);
		puReturnIterate.setType(tIntegerType);
		puReturnShowA = TestPackageStructure.codeFactory.createParameterUnit();
		puReturnShowA.setKind(ParameterKind.RETURN);
		puReturnShowA.setType(tVoidType);
		puReturnShowB = TestPackageStructure.codeFactory.createParameterUnit();
		puReturnShowB.setKind(ParameterKind.RETURN);
		puReturnShowB.setType(tVoidType);
		puReturnShowC = TestPackageStructure.codeFactory.createParameterUnit();
		puReturnShowC.setKind(ParameterKind.RETURN);
		puReturnShowC.setType(tIntegerType);
		puReturnSet = TestPackageStructure.codeFactory.createParameterUnit();
		puReturnSet.setKind(ParameterKind.RETURN);
		puReturnSet.setType(tVoidType);
		puReturnGlobalMethod = TestPackageStructure.codeFactory.createParameterUnit();
		puReturnGlobalMethod.setKind(ParameterKind.RETURN);
		puReturnGlobalMethod.setType(tVoidType);
		puReturnOpen = TestPackageStructure.codeFactory.createParameterUnit();
		puReturnOpen.setKind(ParameterKind.RETURN);
		puReturnOpen.setType(tIntegerType);
		puReturnGlobalMethodNext = TestPackageStructure.codeFactory.createParameterUnit();
		puReturnGlobalMethodNext.setKind(ParameterKind.RETURN);
		puReturnGlobalMethodNext.setType(tVoidType);

		puMain = TestPackageStructure.codeFactory.createParameterUnit();
		puMain.setName("args");
		puMain.setType(tCharArrayType);
		puShowA = TestPackageStructure.codeFactory.createParameterUnit();
		puShowA.setName("count");
		puShowA.setType(tIntegerType);
		puShowB1 = TestPackageStructure.codeFactory.createParameterUnit();
		puShowB1.setName("letter");
		puShowB1.setType(tCharType);
		puShowB2 = TestPackageStructure.codeFactory.createParameterUnit();
		puShowB2.setName("count");
		puShowB2.setType(tIntegerType);
		puSet = TestPackageStructure.codeFactory.createParameterUnit();
		puSet.setName("item");
		puSet.setType(tItemType);
		puOpen1 = TestPackageStructure.codeFactory.createParameterUnit();
		puOpen1.setName("name");
		puOpen1.setType(tCharArrayType);
		puOpen2 = TestPackageStructure.codeFactory.createParameterUnit();
		puOpen2.setName("count");
		puOpen2.setType(tIntegerType);
		puGlobalMethodNext = TestPackageStructure.codeFactory.createParameterUnit();
		puGlobalMethodNext.setName("letter");
		puGlobalMethodNext.setType(iPrintable);

		// Attributes
		StorableUnit suISetable1, suISetable2, suISetable3;
		Attribute attrSuISetable1, attrSuISetable2, attrSuISetable3;

		attrSuISetable1 = TestPackageStructure.kdmFactory.createAttribute();
		attrSuISetable1.setTag("export");
		attrSuISetable1.setValue("private");
		suISetable1 = TestPackageStructure.codeFactory.createStorableUnit();
		suISetable1.setKind(StorableKind.LOCAL);
		suISetable1.setName("length");
		suISetable1.setType(tIntegerType);
		suISetable1.getAttribute().add(attrSuISetable1);

		attrSuISetable2 = TestPackageStructure.kdmFactory.createAttribute();
		attrSuISetable2.setTag("export");
		attrSuISetable2.setValue("protected");
		suISetable2 = TestPackageStructure.codeFactory.createStorableUnit();
		suISetable2.setKind(StorableKind.GLOBAL);
		suISetable2.setName("letter");
		suISetable2.setType(tCharArrayType);
		suISetable2.getAttribute().add(attrSuISetable2);

		attrSuISetable3 = TestPackageStructure.kdmFactory.createAttribute();
		attrSuISetable3.setTag("export");
		attrSuISetable3.setValue("public final");
		suISetable3 = TestPackageStructure.codeFactory.createStorableUnit();
		suISetable3.setKind(StorableKind.STATIC);
		suISetable3.setName("dings");
		suISetable3.setType(cFoo);
		suISetable3.getAttribute().add(attrSuISetable3);

		// Setup structure
		// Packages
		pTest.getCodeElement().add(pEins);
		pTest.getCodeElement().add(pZwei);

		pZwei.getCodeElement().add(pDrei);
		pZwei.getCodeElement().add(pFuenf);
		pZwei.getCodeElement().add(pSechs);
		pZwei.getCodeElement().add(pSieben);
		pZwei.getCodeElement().add(pAcht);
		pZwei.getCodeElement().add(pNeun);
		pZwei.getCodeElement().add(pZehn);

		pDrei.getCodeElement().add(pVier);

		pOrg.getCodeElement().add(pEmf);
		pEmf.getCodeElement().add(pKdm);
		pEmf.getCodeElement().add(pAbc);

		// Classes
		pFuenf.getCodeElement().add(cMain);
		pSieben.getCodeElement().add(cFoo);
		cFoo.getCodeElement().add(cBar);
		pNeun.getCodeElement().add(cInterfaceClass);
		iSetable.getCodeElement().add(cClassInterface);
		pKdm.getCodeElement().add(cDemo);
		pKdm.getCodeElement().add(cManager);
		pAbc.getCodeElement().add(cInput);
		cInput.getCodeElement().add(cStream);

		// Interfaces
		pSechs.getCodeElement().add(iIterator);
		pAcht.getCodeElement().add(iPrintable);
		iPrintable.getCodeElement().add(iWritable);
		cInterfaceClass.getCodeElement().add(iGetable);
		pZehn.getCodeElement().add(iSetable);

		// Dependencies
		iWritable.getCodeRelation().add(extendsIWritable);
		cMain.getCodeRelation().add(importsMain);
		cManager.getCodeRelation().add(importManager);
		cManager.getCodeRelation().add(implementsManager);
		cManager.getCodeRelation().add(extendsManager);
		cGlobalClass.getCodeRelation().add(extendsGlobalClass);

		// Methods
		cMain.getCodeElement().add(mMain);
		iIterator.getCodeElement().add(mIterate);
		cDemo.getCodeElement().add(mShowA);
		cDemo.getCodeElement().add(mShowB);
		cDemo.getCodeElement().add(mShowC);
		cDemo.getCodeElement().add(mSet);
		cManager.getCodeElement().add(mManager);
		cStream.getCodeElement().add(mOpen);

		// Attributes
		iSetable.getCodeElement().add(suISetable1);
		iSetable.getCodeElement().add(suISetable2);
		iSetable.getCodeElement().add(suISetable3);

		// Parameter unit to Signature
		sMain.getParameterUnit().add(puReturnMain);
		sMain.getParameterUnit().add(puMain);
		sIterate.getParameterUnit().add(puReturnIterate);
		sShowA.getParameterUnit().add(puReturnShowA);
		sShowA.getParameterUnit().add(puShowA);
		sShowB.getParameterUnit().add(puReturnShowB);
		sShowB.getParameterUnit().add(puShowB1);
		sShowB.getParameterUnit().add(puShowB2);
		sShowC.getParameterUnit().add(puReturnShowC);
		sSet.getParameterUnit().add(puReturnSet);
		sSet.getParameterUnit().add(puSet);
		sOpen.getParameterUnit().add(puReturnOpen);
		sOpen.getParameterUnit().add(puOpen1);
		sOpen.getParameterUnit().add(puOpen2);
		sGlobalMethod.getParameterUnit().add(puReturnGlobalMethod);
		sGlobalMethodNext.getParameterUnit().add(puReturnGlobalMethodNext);
		sGlobalMethodNext.getParameterUnit().add(puGlobalMethodNext);

		// Signature to method
		mMain.getCodeElement().add(sMain);
		mIterate.getCodeElement().add(sIterate);
		mShowA.getCodeElement().add(sShowA);
		mShowB.getCodeElement().add(sShowB);
		mShowC.getCodeElement().add(sShowC);
		mManager.getCodeElement().add(sManager);
		mSet.getCodeElement().add(sSet);
		mOpen.getCodeElement().add(sOpen);
		mGlobalMethod.getCodeElement().add(sGlobalMethod);
		mGlobalMaethodNext.getCodeElement().add(sGlobalMethodNext);

		// code model
		TestPackageStructure.codeModel.getCodeElement().add(pTest);
		TestPackageStructure.codeModel.getCodeElement().add(pOrg);
		TestPackageStructure.codeModel.getCodeElement().add(cGlobalClass);
		TestPackageStructure.codeModel.getCodeElement().add(iGloablInterface);
		TestPackageStructure.codeModel.getCodeElement().add(mGlobalMethod);
		TestPackageStructure.codeModel.getCodeElement().add(mGlobalMaethodNext);
	}

	/**
	 * Default constructor.
	 */
	private TestPackageStructure() {
		// No code necessary.
	}

	/**
	 * Returns a HashMap containing the package structure.
	 * 
	 * @return
	 *         A HashMap containing the package structure.
	 */
	public static Map<String, Package> getPackages() {
		return TestPackageStructure.packages;
	}

	/**
	 * Returns a HashMap containing class structure.
	 * 
	 * @return
	 *         A HashMap containing class structure.
	 */
	public static Map<String, ClassUnit> getClasses() {
		return TestPackageStructure.classes;
	}

	/**
	 * Returns a HashMap containing the interface structure.
	 * 
	 * @return
	 *         A HashMap containing the interface structure.
	 */
	public static Map<String, InterfaceUnit> getInterfaces() {
		return TestPackageStructure.interfaces;
	}

	/**
	 * Returns a HashMap containing the method structure.
	 * 
	 * @return
	 *         A HashMap containing the method structure.
	 */
	public static Map<String, MethodUnit> getMethods() {
		return TestPackageStructure.methods;
	}

	/**
	 * Returns a CodeModel containing a little structure.
	 * 
	 * @return
	 *         A CodeModel containing a little structure.
	 */
	public static CodeModel getCodeModel() {
		return TestPackageStructure.codeModel;
	}
}

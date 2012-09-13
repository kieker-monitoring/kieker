/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/

package kieker.test.tools.util.kdm;

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
public final class ProvidePackageStructure {
	private static KdmFactory kdmFactory = new KdmFactoryImpl();
	private static CodeFactory codeFactory = new CodeFactoryImpl();
	private static CodeModel codeModel = ProvidePackageStructure.codeFactory.createCodeModel();
	private static Segment segment = ProvidePackageStructure.kdmFactory.createSegment();
	private static Map<String, Package> packages = new HashMap<String, Package>();
	private static Map<String, ClassUnit> classes = new HashMap<String, ClassUnit>();
	private static Map<String, InterfaceUnit> interfaces = new HashMap<String, InterfaceUnit>();
	private static Map<String, MethodUnit> methods = new HashMap<String, MethodUnit>();

	/**
	 * Setup the test data.
	 */
	static {
		// Generate packages
		ProvidePackageStructure.segment.getModel().add(ProvidePackageStructure.codeModel);

		final Package pEins = ProvidePackageStructure.codeFactory.createPackage();
		pEins.setName("eins");
		final Package pZwei = ProvidePackageStructure.codeFactory.createPackage();
		pZwei.setName("zwei");
		final Package pDrei = ProvidePackageStructure.codeFactory.createPackage();
		pDrei.setName("drei");
		final Package pVier = ProvidePackageStructure.codeFactory.createPackage();
		pVier.setName("vier");
		final Package pFuenf = ProvidePackageStructure.codeFactory.createPackage();
		pFuenf.setName("fuenf");
		final Package pSechs = ProvidePackageStructure.codeFactory.createPackage();
		pSechs.setName("sechs");
		final Package pSieben = ProvidePackageStructure.codeFactory.createPackage();
		pSieben.setName("sieben");
		final Package pAcht = ProvidePackageStructure.codeFactory.createPackage();
		pAcht.setName("acht");
		final Package pNeun = ProvidePackageStructure.codeFactory.createPackage();
		pNeun.setName("neun");
		final Package pZehn = ProvidePackageStructure.codeFactory.createPackage();
		pZehn.setName("zehn");
		final Package pTest = ProvidePackageStructure.codeFactory.createPackage();
		pTest.setName("test");
		final Package pKdm = ProvidePackageStructure.codeFactory.createPackage();
		pKdm.setName("kdm");
		final Package pOrg = ProvidePackageStructure.codeFactory.createPackage();
		pOrg.setName("org");
		final Package pEmf = ProvidePackageStructure.codeFactory.createPackage();
		pEmf.setName("emf");
		final Package pAbc = ProvidePackageStructure.codeFactory.createPackage();
		pAbc.setName("abc");

		// Setup package map
		ProvidePackageStructure.packages.put("test", pTest);
		ProvidePackageStructure.packages.put("test.eins", pEins);
		ProvidePackageStructure.packages.put("test.zwei", pZwei);
		ProvidePackageStructure.packages.put("test.zwei.drei", pDrei);
		ProvidePackageStructure.packages.put("test.zwei.drei.vier", pVier);
		ProvidePackageStructure.packages.put("test.zwei.fuenf", pFuenf);
		ProvidePackageStructure.packages.put("test.zwei.sechs", pSechs);
		ProvidePackageStructure.packages.put("test.zwei.sieben", pSieben);
		ProvidePackageStructure.packages.put("test.zwei.acht", pAcht);
		ProvidePackageStructure.packages.put("test.zwei.neun", pNeun);
		ProvidePackageStructure.packages.put("test.zwei.zehn", pZehn);
		ProvidePackageStructure.packages.put("org", pOrg);
		ProvidePackageStructure.packages.put("org.emf", pEmf);
		ProvidePackageStructure.packages.put("org.emf.kdm", pKdm);
		ProvidePackageStructure.packages.put("org.emf.abc", pAbc);

		// Generate classes
		final ClassUnit cMain = ProvidePackageStructure.codeFactory.createClassUnit();
		cMain.setName("Main");
		final ClassUnit cFoo = ProvidePackageStructure.codeFactory.createClassUnit();
		cFoo.setName("Foo");
		final ClassUnit cBar = ProvidePackageStructure.codeFactory.createClassUnit();
		cBar.setName("Bar");
		final ClassUnit cInterfaceClass = ProvidePackageStructure.codeFactory.createClassUnit();
		cInterfaceClass.setName("InterfaceClass");
		final ClassUnit cClassInterface = ProvidePackageStructure.codeFactory.createClassUnit();
		cClassInterface.setName("ClassInterface");
		final ClassUnit cDemo = ProvidePackageStructure.codeFactory.createClassUnit();
		cDemo.setName("Demo");
		final ClassUnit cManager = ProvidePackageStructure.codeFactory.createClassUnit();
		cManager.setName("Manager");
		final ClassUnit cInput = ProvidePackageStructure.codeFactory.createClassUnit();
		cInput.setName("Input");
		final ClassUnit cStream = ProvidePackageStructure.codeFactory.createClassUnit();
		cStream.setName("Stream");
		final ClassUnit cGlobalClass = ProvidePackageStructure.codeFactory.createClassUnit();
		cGlobalClass.setName("GlobalClass");

		// Setup class map
		ProvidePackageStructure.classes.put("test.zwei.fuenf.Main", cMain);
		ProvidePackageStructure.classes.put("test.zwei.sieben.Foo", cFoo);
		ProvidePackageStructure.classes.put("test.zwei.sieben.Foo.Bar", cBar);
		ProvidePackageStructure.classes.put("test.zwei.neun.InterfaceClass", cInterfaceClass);
		ProvidePackageStructure.classes.put("test.zwei.zehn.ISetable.ClassInterface", cClassInterface);
		ProvidePackageStructure.classes.put("org.emf.kdm.Demo", cDemo);
		ProvidePackageStructure.classes.put("org.emf.kdm.Manager", cManager);
		ProvidePackageStructure.classes.put("org.emf.abc.Input", cInput);
		ProvidePackageStructure.classes.put("org.emf.abc.Input.Stream", cStream);
		ProvidePackageStructure.classes.put("GlobalClass", cGlobalClass);

		// Generate Interfaces
		final InterfaceUnit iIterator = ProvidePackageStructure.codeFactory.createInterfaceUnit();
		iIterator.setName("IIterator");
		final InterfaceUnit iPrintable = ProvidePackageStructure.codeFactory.createInterfaceUnit();
		iPrintable.setName("IPrintable");
		final InterfaceUnit iWritable = ProvidePackageStructure.codeFactory.createInterfaceUnit();
		iWritable.setName("IWritable");
		final InterfaceUnit iGetable = ProvidePackageStructure.codeFactory.createInterfaceUnit();
		iGetable.setName("IGetable");
		final InterfaceUnit iSetable = ProvidePackageStructure.codeFactory.createInterfaceUnit();
		iSetable.setName("ISetable");
		final InterfaceUnit iGloablInterface = ProvidePackageStructure.codeFactory.createInterfaceUnit();
		iGloablInterface.setName("IGlobalInterface");

		// Setup interface map
		ProvidePackageStructure.interfaces.put("test.zwei.sechs.IIterator", iIterator);
		ProvidePackageStructure.interfaces.put("test.zwei.acht.IPrintable", iPrintable);
		ProvidePackageStructure.interfaces.put("test.zwei.acht.IPrintable.IWritable", iWritable);
		ProvidePackageStructure.interfaces.put("test.zwei.neun.InterfaceClass.IGetable", iGetable);
		ProvidePackageStructure.interfaces.put("test.zwei.zehn.ISetable", iSetable);
		ProvidePackageStructure.interfaces.put("IGlobalInterface", iGloablInterface);

		// Generate dependencies
		final Imports importManager = ProvidePackageStructure.codeFactory.createImports();
		importManager.setFrom(cManager);
		importManager.setTo(cStream);
		final Imports importsMain = ProvidePackageStructure.codeFactory.createImports();
		importsMain.setFrom(cMain);
		importsMain.setTo(iPrintable);
		final Extends extendsIWritable = ProvidePackageStructure.codeFactory.createExtends();
		extendsIWritable.setFrom(iWritable);
		extendsIWritable.setTo(iIterator);
		final Implements implementsManager = ProvidePackageStructure.codeFactory.createImplements();
		implementsManager.setFrom(cManager);
		implementsManager.setTo(iSetable);
		final Extends extendsManager = ProvidePackageStructure.codeFactory.createExtends();
		extendsManager.setFrom(cManager);
		extendsManager.setTo(cInterfaceClass);
		final Extends extendsGlobalClass = ProvidePackageStructure.codeFactory.createExtends();
		extendsGlobalClass.setFrom(cGlobalClass);
		extendsGlobalClass.setTo(cFoo);

		// Generate methods
		final MethodUnit mMain = ProvidePackageStructure.codeFactory.createMethodUnit();
		mMain.setName("main()");
		mMain.setKind(MethodKind.METHOD);
		final MethodUnit mIterate = ProvidePackageStructure.codeFactory.createMethodUnit();
		mIterate.setName("iterate()");
		mIterate.setKind(MethodKind.METHOD);
		final MethodUnit mShowA = ProvidePackageStructure.codeFactory.createMethodUnit();
		mShowA.setName("showA()");
		mShowA.setKind(MethodKind.METHOD);
		final MethodUnit mShowB = ProvidePackageStructure.codeFactory.createMethodUnit();
		mShowB.setName("showB()");
		mShowB.setKind(MethodKind.METHOD);
		final MethodUnit mShowC = ProvidePackageStructure.codeFactory.createMethodUnit();
		mShowC.setName("showC()");
		mShowC.setKind(MethodKind.METHOD);
		final MethodUnit mManager = ProvidePackageStructure.codeFactory.createMethodUnit();
		mManager.setName("Manager()");
		mManager.setKind(MethodKind.CONSTRUCTOR);
		final MethodUnit mSet = ProvidePackageStructure.codeFactory.createMethodUnit();
		mSet.setName("set()");
		mSet.setKind(MethodKind.METHOD);
		final MethodUnit mOpen = ProvidePackageStructure.codeFactory.createMethodUnit();
		mOpen.setName("open()");
		mOpen.setKind(MethodKind.METHOD);
		final MethodUnit mGlobalMethod = ProvidePackageStructure.codeFactory.createMethodUnit();
		mGlobalMethod.setName("globalMethod()");
		mGlobalMethod.setKind(MethodKind.METHOD);
		final MethodUnit mGlobalMaethodNext = ProvidePackageStructure.codeFactory.createMethodUnit();
		mGlobalMaethodNext.setName("globalMethodNext()");
		mGlobalMaethodNext.setExport(ExportKind.PROTECTED);

		// Setup method map
		ProvidePackageStructure.methods.put("test.zwei.fuenf.Main.main()", mMain);
		ProvidePackageStructure.methods.put("test.zwei.sechs.IIterator.iterate()", mIterate);
		ProvidePackageStructure.methods.put("org.emf.kdm.Demo.showA()", mShowA);
		ProvidePackageStructure.methods.put("org.emf.kdm.Demo.showB()", mShowB);
		ProvidePackageStructure.methods.put("org.emf.kdm.Demo.showC()", mShowC);
		ProvidePackageStructure.methods.put("org.emf.kdm.Manager.Manager()", mManager);
		ProvidePackageStructure.methods.put("org.emf.kdm.Demo.set()", mSet);
		ProvidePackageStructure.methods.put("org.emf.abc.Input.Stream.open()", mOpen);
		ProvidePackageStructure.methods.put("globalMethod()", mGlobalMethod);
		ProvidePackageStructure.methods.put("globalMethodNext()", mGlobalMaethodNext);

		// Method calls
		final CodeRelationship crMain1 = ProvidePackageStructure.codeFactory.createCodeRelationship();
		crMain1.setFrom(mMain);
		crMain1.setTo(mShowA);
		mMain.getCodeRelation().add(crMain1);
		final CodeRelationship crMain2 = ProvidePackageStructure.codeFactory.createCodeRelationship();
		crMain2.setFrom(mMain);
		crMain2.setTo(mOpen);
		mMain.getCodeRelation().add(crMain2);
		final CodeRelationship crMain3 = ProvidePackageStructure.codeFactory.createCodeRelationship();
		crMain3.setFrom(mMain);
		crMain3.setTo(mGlobalMethod);
		mMain.getCodeRelation().add(crMain3);
		final CodeRelationship crShowB = ProvidePackageStructure.codeFactory.createCodeRelationship();
		crShowB.setFrom(mShowB);
		crShowB.setTo(mOpen);
		mShowB.getCodeRelation().add(crShowB);

		// Setup signatures
		final Signature sMain = ProvidePackageStructure.codeFactory.createSignature();
		final Signature sIterate = ProvidePackageStructure.codeFactory.createSignature();
		final Signature sShowA = ProvidePackageStructure.codeFactory.createSignature();
		final Signature sShowB = ProvidePackageStructure.codeFactory.createSignature();
		final Signature sShowC = ProvidePackageStructure.codeFactory.createSignature();
		final Signature sManager = ProvidePackageStructure.codeFactory.createSignature();
		final Signature sSet = ProvidePackageStructure.codeFactory.createSignature();
		final Signature sOpen = ProvidePackageStructure.codeFactory.createSignature();
		final Signature sGlobalMethod = ProvidePackageStructure.codeFactory.createSignature();
		final Signature sGlobalMethodNext = ProvidePackageStructure.codeFactory.createSignature();

		// Types
		final VoidType tVoidType = ProvidePackageStructure.codeFactory.createVoidType();
		tVoidType.setName("void");

		final IntegerType tIntegerType = ProvidePackageStructure.codeFactory.createIntegerType();
		tIntegerType.setName("int");

		final CharType tCharType = ProvidePackageStructure.codeFactory.createCharType();
		tCharType.setName("char");

		final ClassUnit tItemType = ProvidePackageStructure.codeFactory.createClassUnit();
		tItemType.setName("Item");

		final ArrayType tCharArrayType = ProvidePackageStructure.codeFactory.createArrayType();
		tCharArrayType.setName("char[]");
		final IndexUnit indexUnit = ProvidePackageStructure.codeFactory.createIndexUnit();
		final ItemUnit itemUnit = ProvidePackageStructure.codeFactory.createItemUnit();
		indexUnit.setType(tIntegerType);
		itemUnit.setType(tCharType);
		tCharArrayType.setIndexUnit(indexUnit);
		tCharArrayType.setItemUnit(itemUnit);

		// Parameter units
		final ParameterUnit puReturnMain = ProvidePackageStructure.codeFactory.createParameterUnit();
		puReturnMain.setKind(ParameterKind.RETURN);
		puReturnMain.setType(tVoidType);
		final ParameterUnit puReturnIterate = ProvidePackageStructure.codeFactory.createParameterUnit();
		puReturnIterate.setKind(ParameterKind.RETURN);
		puReturnIterate.setType(tIntegerType);
		final ParameterUnit puReturnShowA = ProvidePackageStructure.codeFactory.createParameterUnit();
		puReturnShowA.setKind(ParameterKind.RETURN);
		puReturnShowA.setType(tVoidType);
		final ParameterUnit puReturnShowB = ProvidePackageStructure.codeFactory.createParameterUnit();
		puReturnShowB.setKind(ParameterKind.RETURN);
		puReturnShowB.setType(tVoidType);
		final ParameterUnit puReturnShowC = ProvidePackageStructure.codeFactory.createParameterUnit();
		puReturnShowC.setKind(ParameterKind.RETURN);
		puReturnShowC.setType(tIntegerType);
		final ParameterUnit puReturnSet = ProvidePackageStructure.codeFactory.createParameterUnit();
		puReturnSet.setKind(ParameterKind.RETURN);
		puReturnSet.setType(tVoidType);
		final ParameterUnit puReturnGlobalMethod = ProvidePackageStructure.codeFactory.createParameterUnit();
		puReturnGlobalMethod.setKind(ParameterKind.RETURN);
		puReturnGlobalMethod.setType(tVoidType);
		final ParameterUnit puReturnOpen = ProvidePackageStructure.codeFactory.createParameterUnit();
		puReturnOpen.setKind(ParameterKind.RETURN);
		puReturnOpen.setType(tIntegerType);
		final ParameterUnit puReturnGlobalMethodNext = ProvidePackageStructure.codeFactory.createParameterUnit();
		puReturnGlobalMethodNext.setKind(ParameterKind.RETURN);
		puReturnGlobalMethodNext.setType(tVoidType);

		final ParameterUnit puMain = ProvidePackageStructure.codeFactory.createParameterUnit();
		puMain.setName("args");
		puMain.setType(tCharArrayType);
		final ParameterUnit puShowA = ProvidePackageStructure.codeFactory.createParameterUnit();
		puShowA.setName("count");
		puShowA.setType(tIntegerType);
		final ParameterUnit puShowB1 = ProvidePackageStructure.codeFactory.createParameterUnit();
		puShowB1.setName("letter");
		puShowB1.setType(tCharType);
		final ParameterUnit puShowB2 = ProvidePackageStructure.codeFactory.createParameterUnit();
		puShowB2.setName("count");
		puShowB2.setType(tIntegerType);
		final ParameterUnit puSet = ProvidePackageStructure.codeFactory.createParameterUnit();
		puSet.setName("item");
		puSet.setType(tItemType);
		final ParameterUnit puOpen1 = ProvidePackageStructure.codeFactory.createParameterUnit();
		puOpen1.setName("name");
		puOpen1.setType(tCharArrayType);
		final ParameterUnit puOpen2 = ProvidePackageStructure.codeFactory.createParameterUnit();
		puOpen2.setName("count");
		puOpen2.setType(tIntegerType);
		final ParameterUnit puGlobalMethodNext = ProvidePackageStructure.codeFactory.createParameterUnit();
		puGlobalMethodNext.setName("letter");
		puGlobalMethodNext.setType(iPrintable);

		// Attributes

		final Attribute attrSuISetable1 = ProvidePackageStructure.kdmFactory.createAttribute();
		attrSuISetable1.setTag("export");
		attrSuISetable1.setValue("private");
		final StorableUnit suISetable1 = ProvidePackageStructure.codeFactory.createStorableUnit();
		suISetable1.setKind(StorableKind.LOCAL);
		suISetable1.setName("length");
		suISetable1.setType(tIntegerType);
		suISetable1.getAttribute().add(attrSuISetable1);

		final Attribute attrSuISetable2 = ProvidePackageStructure.kdmFactory.createAttribute();
		attrSuISetable2.setTag("export");
		attrSuISetable2.setValue("protected");
		final StorableUnit suISetable2 = ProvidePackageStructure.codeFactory.createStorableUnit();
		suISetable2.setKind(StorableKind.GLOBAL);
		suISetable2.setName("letter");
		suISetable2.setType(tCharArrayType);
		suISetable2.getAttribute().add(attrSuISetable2);

		final Attribute attrSuISetable3 = ProvidePackageStructure.kdmFactory.createAttribute();
		attrSuISetable3.setTag("export");
		attrSuISetable3.setValue("public final");
		final StorableUnit suISetable3 = ProvidePackageStructure.codeFactory.createStorableUnit();
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
		ProvidePackageStructure.codeModel.getCodeElement().add(pTest);
		ProvidePackageStructure.codeModel.getCodeElement().add(pOrg);
		ProvidePackageStructure.codeModel.getCodeElement().add(cGlobalClass);
		ProvidePackageStructure.codeModel.getCodeElement().add(iGloablInterface);
		ProvidePackageStructure.codeModel.getCodeElement().add(mGlobalMethod);
		ProvidePackageStructure.codeModel.getCodeElement().add(mGlobalMaethodNext);
	}

	/**
	 * Default constructor.
	 */
	private ProvidePackageStructure() {
		// No code necessary.
	}

	/**
	 * Returns a HashMap containing the package structure.
	 * 
	 * @return
	 *         A HashMap containing the package structure.
	 */
	public static Map<String, Package> getPackages() {
		return ProvidePackageStructure.packages;
	}

	/**
	 * Returns a HashMap containing class structure.
	 * 
	 * @return
	 *         A HashMap containing class structure.
	 */
	public static Map<String, ClassUnit> getClasses() {
		return ProvidePackageStructure.classes;
	}

	/**
	 * Returns a HashMap containing the interface structure.
	 * 
	 * @return
	 *         A HashMap containing the interface structure.
	 */
	public static Map<String, InterfaceUnit> getInterfaces() {
		return ProvidePackageStructure.interfaces;
	}

	/**
	 * Returns a HashMap containing the method structure.
	 * 
	 * @return
	 *         A HashMap containing the method structure.
	 */
	public static Map<String, MethodUnit> getMethods() {
		return ProvidePackageStructure.methods;
	}

	/**
	 * Returns a CodeModel containing a little structure.
	 * 
	 * @return
	 *         A CodeModel containing a little structure.
	 */
	public static CodeModel getCodeModel() {
		return ProvidePackageStructure.codeModel;
	}
}

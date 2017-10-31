<?xml version="1.0" encoding="ISO-8859-1" standalone="yes"?>
<TARGET	source="InitPlayer.java"
>
	<ScalingLayout
		layout="412,375,423,414"
	/>
	<ScrollPane
		layout="0,0,412,152"
		maxLayout="0,0,416,152"
		id="scrollpane"
		enabled="true"
		doubleBuffered="true"
		opaque="true"
	/>
	<Group
		layout="220,152,192,88"
		maxLayout="220,152,193,89"
		text="Identit� du Serveur Initiateur"
		name=""
	>
		<ScalingLayout
			layout="192,88,193,89"
		/>
		<TextField
			layout="76,20,101,24"
			maxLayout="76,20,101,24"
			id="serverIP"
			text="BelmamounAdnane"
		/>
		<Label
			layout="8,20,64,24"
			maxLayout="8,20,64,24"
			text="IP du Serveur:"
		/>
		<Label
			layout="40,52,36,24"
			maxLayout="40,52,36,24"
			autoscrolls="false"
			verticalAlignment="0"
			text="Port:"
		/>
		<TextField
			layout="76,52,101,24"
			maxLayout="76,52,101,24"
			id="serverPort"
			text="1630"
		/>
	</Group>
	<Group
		layout="0,152,220,88"
		maxLayout="0,152,221,89"
		text="Identit� des Partis"
	>
		<ScalingLayout
			layout="220,88,221,89"
		/>
		<Label
			layout="12,56,100,20"
			maxLayout="12,56,100,20"
			text="A travers le port:"
		/>
		<Label
			layout="16,16,88,24"
			maxLayout="16,16,88,24"
			text="Mon identit� est:"
		/>
		<TextField
			layout="104,16,96,24"
			maxLayout="104,16,96,24"
			id="myIdentity"
			text="Alice ou Zaynab"
		/>
		<TextField
			layout="104,52,96,24"
			maxLayout="104,52,96,24"
			id="clientPort"
			text="1730"
			toolTipText=""
		/>
	</Group>
	<Group
		layout="0,240,412,68"
		maxLayout="0,240,413,69"
		text="Configuration du Protocol"
	>
		<ScalingLayout
			layout="412,68,413,69"
		/>
		<Button
			layout="328,40,68,24"
			maxLayout="328,40,68,24"
			id="cleanup"
			label="Nettoyage"
			border="RaisedBevelBorder"
			toolTipText="Permet de nettoyer le fichier index"
			verticalAlignment="0"
			events="actionPerformed"
		/>
		<Button
			layout="328,12,68,24"
			maxLayout="328,12,68,24"
			id="outputFileButton"
			label="Aucune sortie"
			border="RaisedBevelBorder"
			toolTipText="selectionner le r�pertoire/fichier de sortie, annuler ne donnera aucune sortie"
			font="Dialog-bold-10"
			events="actionPerformed"
		/>
		<Button
			layout="256,40,68,24"
			maxLayout="256,40,68,24"
			id="inspect"
			label="V�rifier"
			border="RaisedBevelBorder"
			toolTipText="V�rification de la configuration"
			actionCommand="Inspect"
			events="actionPerformed"
		/>
		<Button
			layout="256,12,68,24"
			maxLayout="256,12,68,24"
			id="configButton"
			font="Serif-bold-12"
			label="Configurer"
			border="RaisedBevelBorder"
			autoscrolls="false"
			borderPainted="true"
			horizontalTextPosition="11"
			icon="vcontrol.gif"
			selectedIcon="tools.gif"
			toolTipText="configuration des param�tres du protocol"
			opaque="true"
			events="actionPerformed"
		/>
		<ComboBox
			layout="8,24,240,24"
			maxLayout="8,24,240,24"
			id="protSelection"
			name="selection du Protocol "
			editable="false"
			toolTipText="Selectionner un protocol avant de configurer"
			actionCommand="newProtocol"
			events="itemStateChanged"
		/>
	</Group>
	<Group
		layout="0,308,412,64"
		maxLayout="0,308,413,65"
		text="Actions"
	>
		<ScalingLayout
			layout="412,64,413,65"
		/>
		<ProgressBar
			layout="220,40,180,12"
			maxLayout="220,40,180,12"
			id="progressBar"
			border="RaisedBevelBorder"
			foreground="ff19ff1c"
			name="Bar de progression"
			string="0%"
		/>
		<RadioButton
			layout="16,16,88,40"
			maxLayout="16,16,88,40"
			id="startButton"
			contentAreaFilled="true"
			label="D�marrer"
			selectedIcon="radioSelected.gif"
			pressedIcon="radioPressed.gif"
			icon="radio.gif"
			actionCommand="a RadioButton"
			events="actionPerformed"
		/>
		<RadioButton
			layout="120,20,76,36"
			maxLayout="120,20,76,36"
			id="stopButton"
			label="Arreter"
			selectedIcon="radioSelected.gif"
			pressedIcon="radioPressed.gif"
			icon="radio.gif"
			actionCommand="stopButton"
			borderPainted="false"
			events="actionPerformed"
		/>
		<Label
			layout="224,12,172,32"
			maxLayout="228,12,172,32"
			id="progressLabel"
			horizontalTextPosition="11"
			enabled="true"
			horizontalAlignment="10"
			text=""
			requestFocusEnabled="true"
			doubleBuffered="false"
			foreground="ff000000"
			debugGraphicsOptions="0"
			opaque="false"
		/>
	</Group>
	<TextArea
		layout="0,0,412,150"
		maxLayout="0,4,416,146"
		id="textarea"
		text=""
		editable="false"
	/>
</TARGET>

<?xml version="1.0" encoding="ISO-8859-1" standalone="yes"?>
<TARGET	source="InitServer.java"
>
	<ScalingLayout
		layout="373,319,380,320"
	/>
	<TextArea
		layout="0,0,372,132"
		maxLayout="0,4,372,136"
		id="_textarea"
		text=""
	/>
	<ScrollPane
		layout="0,0,372,132"
		maxLayout="0,0,372,132"
		id="scrollpane"
	/>
	<Group
		layout="0,256,372,60"
		maxLayout="0,256,373,61"
		text="D�marrage/Arr�t"
	>
		<ScalingLayout
			layout="372,60,373,61"
		/>
		<RadioButton
			layout="16,16,72,36"
			maxLayout="16,16,72,36"
			id="stopButton"
			label="Arr�t"
			selectedIcon="radioSelected.gif"
			pressedIcon="radioPressed.gif"
			icon="radio.gif"
			debugGraphicsOptions="0"
			events="actionPerformed"
		/>
		<RadioButton
			layout="108,12,76,44"
			maxLayout="108,12,76,44"
			id="startButton"
			label="D�marrage"
			selectedIcon="src/qrypto/gui/images/radioSelected.gif"
			pressedIcon="src/qrypto/gui/images/radioPressed.gif"
			icon="src/qrypto/gui/images/radio.gif"
			events="actionPerformed"
		/>
		<Label
			layout="212,20,140,16"
			maxLayout="212,20,140,16"
			text="Progression Transmission."
			toolTipText=""
		/>
		<ProgressBar
			layout="208,40,140,12"
			maxLayout="208,40,140,12"
			id="progressBar"
			border="RaisedBevelBorder"
			foreground="ff19ff1c"
			name="progressBar"
			string="0%"
		/>
	</Group>
	<Group
		layout="0,196,372,60"
		maxLayout="0,196,373,61"
		text="Param�tres des Ports"
	>
		<ScalingLayout
			layout="372,60,373,61"
		/>
		<TextField
			layout="268,20,84,24"
			maxLayout="268,20,84,24"
			id="_portcfield"
		/>
		<Label
			layout="188,20,80,24"
			maxLayout="188,20,80,24"
			text="Port du Client:"
		/>
		<TextField
			layout="92,20,84,24"
			maxLayout="92,20,84,24"
			id="_portsfield"
		/>
		<Label
			layout="8,20,84,24"
			maxLayout="8,20,84,24"
			text="Port du Serveur:"
		/>
	</Group>
	<Group
		layout="0,136,372,60"
		maxLayout="0,136,373,61"
		text="Configuration de la Transmission Quantique"
	>
		<ScalingLayout
			layout="372,60,373,61"
		/>
		<Button
			layout="152,20,68,24"
			maxLayout="152,20,68,24"
			id="configButton"
			label="Config"
			border="RaisedBevelBorder"
			events="actionPerformed"
		/>
		<Label
			layout="232,16,120,32"
			maxLayout="232,16,120,32"
			id="infoLabel"
			text=""
			toolTipText=""
		/>
		<CheckBox
			layout="88,20,60,28"
			maxLayout="88,20,60,28"
			id="_chkrqc"
			label="Reel"
			actionCommand="real"
			border="EtchedBorder"
		/>
		<CheckBox
			layout="12,20,68,28"
			maxLayout="12,20,68,28"
			id="_chckvqc"
			label="Virtuel"
			actionCommand="virtual"
			toolTipText=""
		/>
	</Group>
</TARGET>

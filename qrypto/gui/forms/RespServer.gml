<?xml version="1.0" encoding="ISO-8859-1" standalone="yes"?>
<TARGET	source="RespServer.java"
>
	<ScalingLayout
		layout="373,339,380,348"
	/>
	<ScrollPane
		layout="0,0,372,132"
		maxLayout="0,0,372,132"
		id="scrollpane"
	/>
	<Group
		layout="0,276,372,60"
		maxLayout="0,276,373,61"
		text="Actions"
	>
		<ScalingLayout
			layout="372,60,373,61"
		/>
		<RadioButton
			layout="16,16,72,36"
			maxLayout="16,16,72,36"
			id="stopButton"
			label="Arr�ter"
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
			label="D�marrer"
			selectedIcon="radioSelected.gif"
			pressedIcon="radioPressed.gif"
			icon="radio.gif"
			events="actionPerformed"
		/>
		<Label
			layout="248,20,68,16"
			maxLayout="248,20,68,16"
			text="Progression de la transmission"
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
		layout="0,136,372,60"
		maxLayout="0,136,372,60"
		text="Configuration de la Transmission Quantique"
	>
		<ScalingLayout
			layout="372,60,372,60"
		/>
		<Label
			layout="160,24,196,20"
			maxLayout="160,24,196,20"
			id="qType"
			requestFocusEnabled="true"
			opaque="false"
			border="EmptyBorder"
			foreground="ff000000"
			toolTipText=""
			text="non d�fini"
			font="Dialog-bold-0"
		/>
		<Button
			layout="88,24,60,24"
			maxLayout="88,24,60,24"
			id="configVirtualButton"
			label="Virtuel "
			border="RaisedBevelBorder"
			horizontalAlignment="0"
			events="actionPerformed"
		/>
		<Button
			layout="16,24,56,24"
			maxLayout="16,24,56,24"
			id="configRealButton"
			label="R�el"
			border="RaisedBevelBorder"
			horizontalAlignment="0"
			events="actionPerformed"
		/>
	</Group>
	<Group
		layout="0,196,372,80"
		maxLayout="0,196,373,81"
		text="Configuration des ports"
	>
		<ScalingLayout
			layout="372,80,373,81"
		/>
		<TextField
			layout="248,20,112,24"
			maxLayout="248,20,112,24"
			id="_IPfield"
		/>
		<Label
			layout="180,20,68,24"
			maxLayout="180,20,68,24"
			text="IP du Serveur :"
		/>
		<TextField
			layout="92,48,84,24"
			maxLayout="92,48,84,24"
			id="_portcfield"
		/>
		<Label
			layout="12,48,80,24"
			maxLayout="12,48,80,24"
			text="Port du Client :"
		/>
		<TextField
			layout="92,20,84,24"
			maxLayout="92,20,84,24"
			id="_portsfield"
		/>
		<Label
			layout="8,20,84,24"
			maxLayout="8,20,84,24"
			text="Port du Serveur :"
		/>
	</Group>
	<TextArea
		layout="0,0,372,132"
		maxLayout="0,0,372,132"
		id="_textarea"
		text=""
	/>
</TARGET>
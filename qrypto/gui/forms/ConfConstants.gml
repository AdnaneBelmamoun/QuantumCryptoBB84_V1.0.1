<?xml version="1.0" encoding="ISO-8859-1" standalone="yes"?>
<TARGET	source="ConfConstants.java"
>
	<ScalingLayout
		layout="406,532,630,478"
	/>
	<Group
		layout="0,328,404,84"
		maxLayout="0,328,404,84"
		text="Lancer configuration"
	>
		<ScalingLayout
			layout="404,84,404,84"
		/>
		<CheckBox
			layout="280,16,112,20"
			maxLayout="280,16,112,20"
			id="fakeDGCB"
			label="Fake DG"
			foreground="ff0e4b88"
			toolTipText="check fake DG par default"
		/>
		<CheckBox
			layout="144,44,124,20"
			maxLayout="144,44,124,20"
			id="respPlayerCB"
			label="Repondeur"
			foreground="ff0e4b88"
			toolTipText="check Répondeur par default"
			opaque="true"
		/>
		<CheckBox
			layout="144,16,128,20"
			maxLayout="144,16,128,20"
			id="initPlayerCB"
			label="Initiateur"
			foreground="ff0e4b88"
			margin="2,2,2,2"
			toolTipText="check initiateur par default"
		/>
		<CheckBox
			layout="12,44,116,20"
			maxLayout="12,44,116,20"
			id="respServerCB"
			label="Serveur Répondeur"
			foreground="ff0e4b88"
			toolTipText="check Serveur Répondeur par default"
		/>
		<CheckBox
			layout="12,16,116,20"
			maxLayout="12,16,116,20"
			id="initServerCB"
			label="Serveur initiateur"
			foreground="ff1159a1"
			toolTipText="check init server by default"
		/>
	</Group>
	<Group
		layout="216,416,188,68"
		maxLayout="216,416,188,68"
		text="Fake DG"
	>
		<ScalingLayout
			layout="188,68,188,68"
		/>
		<TextField
			layout="104,24,68,20"
			maxLayout="104,24,68,20"
			id="dg2dgport"
			toolTipText="port pour communication DG 2 DG "
		/>
		<Label
			layout="20,24,76,20"
			maxLayout="20,24,76,20"
			font="Dialog-plain-12"
			text="port utile"
		/>
	</Group>
	<Group
		layout="0,416,216,100"
		maxLayout="0,416,216,100"
		text="Génération HTML "
	>
		<ScalingLayout
			layout="216,100,216,100"
		/>
		<Label
			layout="8,16,90,24"
			maxLayout="8,16,90,24"
			text="Répértoire d'entrée:"
		/>
		<Label
			layout="8,40,200,20"
			maxLayout="8,40,200,20"
			id="tempDirLabel"
			font="Dialog-plain-10"
			foreground="ff000000"
			toolTipText="directory where to find HTML templates"
		/>
		<Button
			layout="116,64,92,24"
			maxLayout="116,64,92,24"
			id="templateDir"
			label="Changer Répértoire"
			events="actionPerformed"
		/>
	</Group>
	<Button
		layout="320,488,80,24"
		maxLayout="320,488,80,24"
		id="cancel"
		label="Annuler"
		events="actionPerformed"
	/>
	<Button
		layout="220,488,80,24"
		maxLayout="220,488,80,24"
		id="save"
		label="Enregistrer"
		events="actionPerformed"
	/>
	<Group
		layout="0,188,404,56"
		maxLayout="0,188,404,56"
		text="Configuration par défaut de l'initiateur"
	>
		<ScalingLayout
			layout="404,56,404,56"
		/>
		<Label
			layout="244,20,60,20"
			maxLayout="244,20,60,20"
			font="Dialog-plain-11"
			text="port"
		/>
		<TextField
			layout="328,20,68,20"
			maxLayout="328,20,68,20"
			id="Port d'initiateur"
			toolTipText="le port surle quel l'initiateur écoute pour le Répondeur"
		/>
		<TextField
			layout="112,20,112,20"
			maxLayout="112,20,112,20"
			id="IP d'initiateur"
			toolTipText="L'IP par defaut sur laquelle l'initiateur s'éxècute"
		/>
		<Label
			layout="8,20,90,20"
			maxLayout="8,20,90,20"
			font="Dialog-plain-11"
			text="Init Player IP"
		/>
	</Group>
	<Group
		layout="0,104,404,80"
		maxLayout="0,104,404,80"
		text="Paramétres par défaut du Serveur Répondeur"
	>
		<ScalingLayout
			layout="404,80,404,80"
		/>
		<Label
			layout="240,48,76,20"
			maxLayout="240,48,76,20"
			font="Dialog-plain-11"
			text="DG port IP "
		/>
		<TextField
			layout="328,48,68,20"
			maxLayout="328,48,68,20"
			id="respDGport"
			toolTipText="le port par défaut ou le DG répondeur écoute pour le serveur réponse"
		/>
		<TextField
			layout="328,20,68,20"
			maxLayout="328,20,68,20"
			id="respscport"
			toolTipText="le port ou leserveur répondeur attent pour l'exécutant (l'initiateur)"
		/>
		<Label
			layout="240,20,90,20"
			maxLayout="240,20,90,20"
			font="Dialog-plain-11"
			text="Port Client "
		/>
		<TextField
			layout="112,48,112,20"
			maxLayout="112,48,112,20"
			id="rdgIP"
			toolTipText="l'ip par défaut ou le DG repondeur s'éxècute"
		/>
		<Label
			layout="8,48,64,20"
			maxLayout="12,48,64,20"
			font="Dialog-plain-11"
			text="DG IP"
		/>
		<TextField
			layout="112,20,112,20"
			maxLayout="112,20,112,20"
			id="respserverIP"
			toolTipText="l'ip ou le serveur reponse s'éxècute"
		/>
		<Label
			layout="8,20,108,20"
			maxLayout="8,20,108,20"
			font="Dialog-plain-11"
			text="Resp server IP"
		/>
	</Group>
	<Group
		layout="0,4,404,96"
		maxLayout="0,4,404,96"
		text="Config parr défaut du serveur initiateur"
	>
		<ScalingLayout
			layout="404,96,404,96"
		/>
		<TextField
			layout="328,12,68,20"
			maxLayout="328,20,68,20"
			id="initscport"
			toolTipText="le port ou le serveur initiateur écoute pour l'exècutant"
		/>
		<Label
			layout="240,12,76,20"
			maxLayout="240,12,76,20"
			font="Dialog-plain-11"
			text="port Client "
		/>
		<Label
			layout="240,72,84,16"
			maxLayout="240,72,84,16"
			font="Dialog-plain-11"
			text="port Init DG "
		/>
		<TextField
			layout="328,68,68,20"
			maxLayout="328,68,68,20"
			id="initDGport"
			toolTipText="le port par défaut ou l'initiateur DG ecoute pour le serveur initiateur"
		/>
		<TextField
			layout="328,40,68,20"
			maxLayout="328,40,68,20"
			id="initssport"
			toolTipText="le port ou le serveur initiateurecoute pour le serveur réponse"
		/>
		<Label
			layout="240,44,80,12"
			maxLayout="240,44,80,12"
			font="Dialog-plain-11"
			text="Port Serveur "
		/>
		<Label
			layout="8,24,90,20"
			maxLayout="8,24,90,20"
			font="Dialog-plain-11"
			text="IP serveur Init  "
		/>
		<Label
			layout="8,64,90,20"
			maxLayout="8,64,90,20"
			font="Dialog-plain-11"
			text="IP DG Init  "
		/>
		<TextField
			layout="100,64,116,20"
			maxLayout="100,64,116,20"
			id="idgIP"
			toolTipText="L'ip adresse ou le DG initiateur ecoute pour le serveur initiateur"
		/>
		<TextField
			layout="100,24,116,20"
			maxLayout="100,24,116,20"
			id="initserverIP"
			toolTipText="L'adresse ip par défaut ou le serveur initiateur s'éxecute"
		/>
	</Group>
	<Group
		layout="0,244,404,80"
		maxLayout="0,244,404,80"
		text="Autres"
	>
		<ScalingLayout
			layout="404,80,404,80"
		/>
		<Label
			layout="196,48,132,20"
			maxLayout="196,48,132,20"
			font="Dialog-plain-12"
			text="Erreur..Probléme avec les Détecteurs ."
		/>
		<TextField
			layout="328,48,68,20"
			maxLayout="332,48,64,20"
			id="errorprob"
			toolTipText="Taille par défaut du bouquet pour Transmission Quantique Réelle ."
		/>
		<TextField
			layout="328,16,68,20"
			maxLayout="328,16,68,20"
			id="deftransrate"
			toolTipText="Taux de transmission d'une transmission quantique réelle Corrompue."
		/>
		<Label
			layout="220,16,100,20"
			maxLayout="224,20,100,20"
			font="Dialog-plain-12"
			text="Taux de la Transmission corrompue"
		/>
		<TextField
			layout="116,28,64,20"
			maxLayout="116,28,64,20"
			id="bucksize"
			toolTipText="Taille par défaut du bouquet de Qbits  pour transmission quantique réelle."
		/>
		<Label
			layout="16,28,84,20"
			maxLayout="16,28,84,20"
			font="Dialog-plain-12"
			text="Taille Bouquet"
		/>
	</Group>
</TARGET>

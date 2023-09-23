plugins {
	id("com.android.application")
	id("org.jetbrains.kotlin.android")
	id("com.google.devtools.ksp")
}

android {
	namespace = "com.jinu.todoandmodes"
	compileSdk = 33


	defaultConfig {
		applicationId = "com.jinu.todoandmodes"
		minSdk = 24
		targetSdk = 33
		versionCode = 1
		versionName = "1.0"
		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
	}

	buildTypes {
		release {
			isMinifyEnabled = true
			isShrinkResources = true
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}
	kotlinOptions {
		jvmTarget = "17"
	}
	buildFeatures {
		viewBinding = true
	}
}

dependencies {

	//noinspection GradleDependency
	implementation("androidx.core:core-ktx:1.9.0")
	implementation("androidx.appcompat:appcompat:1.6.1")
	implementation("com.google.android.material:material:1.9.0")
	implementation("androidx.constraintlayout:constraintlayout:2.1.4")
	implementation("com.google.android.gms:play-services-ads:22.2.0")
	testImplementation("junit:junit:4.13.2")
	androidTestImplementation("androidx.test.ext:junit:1.1.5")
	androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

	//room

	val room_version = "2.5.2"
	implementation("androidx.room:room-runtime:$room_version")
	annotationProcessor("androidx.room:room-compiler:$room_version")
	ksp("androidx.room:room-compiler:$room_version")


	//viewModel
	val lifecycle_version = "2.6.2"
	implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
	implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycle_version")
	implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
	implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version")
	implementation("androidx.lifecycle:lifecycle-runtime-compose:$lifecycle_version")
	implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle_version")

	//other layouts
	implementation("com.patrykandpatrick.vico:views:1.9.2")
}

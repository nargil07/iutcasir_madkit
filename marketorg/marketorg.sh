#!/bin/bash
java -cp ../../lib/boot.jar -Xms32m -Xmx128m "-Dpython.home=lib" madkit.boot.Madkit madkit.kernel.Booter --graphics --config marketorg.cfg
		
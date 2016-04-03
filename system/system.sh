#!/bin/bash
java -cp ../../lib/boot.jar -Xms64m -Xmx256m madkit.boot.Madkit madkit.kernel.Booter --graphics --config system.cfg
		
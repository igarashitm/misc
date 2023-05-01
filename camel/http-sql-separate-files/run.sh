#!/bin/bash

camel run beans.yaml actor.yaml city.yaml --deps=mvn:org.postgresql:postgresql:42.6.0

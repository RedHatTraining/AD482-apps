import React from 'react';
import {
	Card,
	CardTitle,
	CardBody,
	CardFooter,
	Grid,
	GridItem,
	Button,
  DescriptionList,
  DescriptionListGroup,
  DescriptionListTerm,
  DescriptionListDescription
} from '@patternfly/react-core';
import { WindTurbine } from '../models/WindTurbine';


interface WindTurbineProps {
	turbine: WindTurbine,
	production: {
		megawatts: number
	}
}


const WindTurbineCard = (props: WindTurbineProps): JSX.Element => {

	function renderAnimationStyle(mwatts: number) {
		let animation;
		if (mwatts === 0) {
			animation = "none";
		} else {
			const interval = 100 / mwatts;
			animation = "spin " + interval + "s linear infinite";
		}
		return { animation }
	}

	return (
		<Card id="card-demo-horizontal-split-example" isFlat>
			<Grid hasGutter>
				<GridItem className="turbineGraphic" span={2}>
					<svg width="200px" height="200px" viewBox="0.0 0.0 284.0 360.0" xmlns="http://www.w3.org/2000/svg">
						<clipPath id="p.0">
							<path d="m0 0l284.0 0l0 360.0l-284.0 0l0 -360.0z" clipRule="nonzero" />
						</clipPath>
						<path fill="#000000" fillOpacity="0.0" d="m0 0l284.0 0l0 360.0l-284.0 0z" fillRule="evenodd" />
						<path fill="#666666" d="m125.684975 357.49393l7.1732254 -172.25195l14.346451 0l7.173233 172.25195z"
							fillRule="evenodd" />
						<g className="turbineGraphicBlades" style={renderAnimationStyle(props.production.megawatts)}>
							<path fill="#f3f3f3"
								d="m119.00193 157.90239l0 0c0 -11.576233 9.384392 -20.960632 20.960625 -20.960632l0 0c5.5591125 0 10.890518 2.2083435 14.821411 6.139221c3.9308777 3.930893 6.139221 9.262299 6.139221 14.821411l0 0c0 11.576233 -9.384399 20.960632 -20.960632 20.960632l0 0c-11.576233 0 -20.960625 -9.384399 -20.960625 -20.960632z"
								fillRule="evenodd" />
							<path fill="#d9d9d9"
								d="m29.356571 259.56705l94.54401 -79.343994l-10.283203 -12.255997l-38.6464 11.547195l-55.4832 77.892z"
								fillRule="evenodd" />
							<path fill="#d9d9d9" d="m132.4655 8.670863l0 123.296l16.0 0l16.0 -36.988792l-24.0 -92.472z"
								fillRule="evenodd" />
							<path fill="#d9d9d9"
								d="m274.59222 225.16309l-106.895996 -61.727997l-8.0 13.855988l24.068802 32.374405l92.17201 25.511993z"
								fillRule="evenodd" />
						</g>
					</svg>
				</GridItem>
				<GridItem span={4}>
					<CardTitle>{props.turbine.description}</CardTitle>
					<CardBody>
            <DescriptionList>
              <DescriptionListGroup>
                <DescriptionListTerm>Measurements processed</DescriptionListTerm>
                <DescriptionListDescription>67</DescriptionListDescription>
              </DescriptionListGroup>
            </DescriptionList>
					</CardBody>
					<CardFooter>
						<Button variant="tertiary">Call to action</Button>
					</CardFooter>
				</GridItem>
			</Grid>
		</Card>
	);
}

export { WindTurbineCard };
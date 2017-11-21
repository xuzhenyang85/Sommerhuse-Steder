import React, { Component } from 'react'
import Data from "../facades/adminFacade";

class Locations extends Component {
  constructor(){
    super();
    this.state = { data: [], err: "" }
  }

  componentWillMount() {
    /*
    This will fetch data each time you navigate to this route
    If only required once, add "logic" to determine when data should be "refetched"
    */
    Data.getPlaces((e, data) => {
      if (e) {
        return this.setState({ err: e.err })
      }
      this.setState({ err: "", data });
    });
  }
  render() {

  const data = this.state.data.map(function(element){
    return <ul key={element.id}>{element.street}
                <li key={element.id}>Zip: {element.zip}</li>
                <li key={element.id}>Beskrivelse: {element.description}</li>
                <li key={element.id}>Longtitude: {element.longtitude}</li>
                <li key={element.id}>Latitude: {element.latitude}</li>
                <li key={element.id}>Rating: {element.rating}</li>
            </ul>;
    }
  );

    return (
      <div>
        <h2>Places</h2>
        <p>This message is fetched from the server if you are properly logged in</p>

        <div>
          {data}
        </div>
        { this.state.err && (
          <div className="alert alert-danger errmsg-left" role="alert">
            {this.state.err}
          </div>
        )}

      </div>
    )
  }
}

export default Locations;

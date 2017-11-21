import React, { Component } from 'react'
import auth from '../authorization/auth'


class Register extends Component {
  constructor() {
    super();
    this.state = { err: "", user: {username:"",password:""} }
  }

  onChange = (e) => {
    const propertyName = e.target.id;
    const value = e.target.value;
    let user = this.state.user;
    user[propertyName] = value;
    this.setState({user});
  }

  handleSubmit = (event) => {
    event.preventDefault()
    const user = this.state.user.username;
    const pass = this.state.user.password;

    auth.register(user, pass, (err, loggedIn) => {
      if (err) {
        return this.setState({ err: err.errorMessage });
      }
      this.setState({ err: "" });
      this.props.history.push("/");
    });
    setTimeout( function(){
      auth.login( user, pass, (err, loggedIn) => {
      });
    }, 2000);
  }



  render() {
    return (
      <div className="container">
        <form className="form-register" onSubmit={this.handleSubmit}>
          <h2 className="form-register-heading">Please Register </h2>
          <label htmlFor="inputEmail" className="sr-only">Email address</label>
          <input type="text" value={this.state.user.username} onChange={this.onChange} className="form-control" id="username" placeholder="User Name" required autoFocus />
          <label htmlFor="inputPassword" className="sr-only">Password</label>
          <input type="password" value={this.state.user.password} onChange={this.onChange} id="password" className="form-control" placeholder="Password" required />
          <button className="btn btn-lg btn-primary btn-block" type="submit">Register</button>
          <br />
        </form>
        { this.state.err && (
          <div className="alert alert-danger errmsg"  role="alert">
            {this.state.err}
          </div>
        )}
      </div>
    )
  }
}

export default Register;

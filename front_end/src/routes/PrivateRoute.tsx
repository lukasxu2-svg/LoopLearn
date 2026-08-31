import React, { ReactNode } from 'react'
import { Navigate } from 'react-router-dom'

interface Props {
  isAuthenticated: boolean
  children: ReactNode
}

const PrivateRoute = ({ isAuthenticated, children }: Props) => {
  if (!isAuthenticated) {
    return <Navigate to="/login" replace />
  }

  return <>{children}</>
}

export default PrivateRoute
